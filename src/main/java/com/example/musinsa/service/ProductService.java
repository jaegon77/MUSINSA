package com.example.musinsa.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.musinsa.common.constant.ProductCommonConstants;
import com.example.musinsa.common.exception.CustomException;
import com.example.musinsa.common.util.Util;
import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoriesForLowestPriceDto;
import com.example.musinsa.dto.CategoryLowestHighestPriceDto;
import com.example.musinsa.repository.CategoryRepository;
import com.example.musinsa.repository.ProductRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public Map<String, Object> getLowestPriceProductsByCategory() {
		// Step 1: 카테고리 별 최저가격 브랜드와 상품 가격 조회
		List<CategoriesForLowestPriceDto> products = productRepository.findLowestPriceProductsByCategory();

		// Validation 1: 데이터 유효성 확인
		if (products.isEmpty()) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "No products found for the given brand.");
		}

		// Validation 2: 카테고리 수 확인
		if (products.size() != ProductCommonConstants.CATEGORY_COUNT) {
			throw new CustomException(HttpStatus.SERVICE_UNAVAILABLE, "Category count mismatch. Some categories are missing.");
		}

		// Validation 2: 상품의 음수 가격 확인
		if (products.stream().anyMatch(product -> product.getPrice() < 0)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid price data found. Price cannot be negative.");
		}

		// Step 2: 카테고리 별 상품의 최저가격 조회
		int totalPrice = products.stream().mapToInt(CategoriesForLowestPriceDto::getPrice).sum();

		// Step 3: 결과 구성
		Map<String, Object> result = new HashMap<>();
		result.put("categories", products);
		result.put("totalPrice", Util.PriceFormatterUtil(totalPrice));
		result.put("message", "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회.");
		return result;
	}

	public Map<String, Object> getLowestPriceProductsByBrand() {
		// Step 1: 각 브랜드별 카테고리 상품의 총액 계산
		List<BrandTotalPriceDto> brandTotals = productRepository.findBrandTotalPrices();

		// Validation 1: 데이터 유효성 확인
		if (brandTotals.isEmpty()) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "No data available for the requested operation.");
		}

		// Step 2 && Validation 2: 최저가 브랜드 찾기
		BrandTotalPriceDto lowestBrand = brandTotals.stream()
				.min(Comparator.comparingInt(BrandTotalPriceDto::getTotalPrice))
				.orElseThrow(() -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "No lowest price brand found."));

		// Step 3: 해당 브랜드의 카테고리별 상품 정보 조회
		List<BrandTotalPriceDto.LowestPriceDto> products = productRepository.findProductsByBrand(lowestBrand.getBrandName());

		// Validation 3: 모든 카테고리 상품이 포함되어 있는지 확인
		if (products.size() != ProductCommonConstants.CATEGORY_COUNT) {
			throw new CustomException(HttpStatus.SERVICE_UNAVAILABLE, "Some categories are missing for the selected brand.");
		}
		lowestBrand.setLowestPriceDto(products);

		// Step 4: 결과 구성
		Map<String, Object> result = new HashMap<>();
		result.put("brand", lowestBrand.getBrandName());
		result.put("categories", lowestBrand.getLowestPriceDto());
		result.put("totalPrice", Util.PriceFormatterUtil(lowestBrand.getTotalPrice()));
		result.put("message", "총합 최저가 단일 브랜드로 모든 카테고리 상품의 가격 조회.");
		return result;
	}

	public Map<String, Object> getHighestAndLowestPriceByCategory(String categoryName) {
		// Validation 1: 문자열 유효성 검사
		if (StringUtils.isBlank(categoryName)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Category name cannot be blank.");
		}

		// Step 1 && Validation 2: 카테고리 존재 확인
		if (!categoryRepository.existsByName(categoryName)) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Category name does not exist.");
		}

		CategoryLowestHighestPriceDto categoryLowestHighestPriceDto = new CategoryLowestHighestPriceDto();

		// Step 2: 특정 카테고리의 최저, 최고 가격 조회
		CategoryLowestHighestPriceDto.LowestPriceByCategory lowestPriceByCategory = productRepository.findLowestPriceByCategory(categoryName);
		CategoryLowestHighestPriceDto.HighestPriceByCategory highestPriceByCategory = productRepository.findHighestPriceByCategory(categoryName);

		// Validation 3: 최저, 최고 가격 상품 존재 확인
		if (lowestPriceByCategory == null || highestPriceByCategory == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "No data found for the given category: " + categoryName);
		}

		categoryLowestHighestPriceDto.setCategory(categoryName);
		categoryLowestHighestPriceDto.setLowestPriceByCategory(lowestPriceByCategory);
		categoryLowestHighestPriceDto.setHighestPriceByCategory(highestPriceByCategory);

		// Step 3: 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
		Map<String, Object> result = new HashMap<>();
		result.put("category", categoryLowestHighestPriceDto.getCategory());
		result.put("lowestPrice", categoryLowestHighestPriceDto.getLowestPriceByCategory());
		result.put("highestPrice", categoryLowestHighestPriceDto.getHighestPriceByCategory());
		result.put("message", "최저가 및 최고가 브랜드와 상품 가격 조회 완료.");
		return result;
	}
}
