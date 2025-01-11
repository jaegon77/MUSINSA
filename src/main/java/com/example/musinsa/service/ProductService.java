package com.example.musinsa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.musinsa.common.constant.ProductCommonConstants;
import com.example.musinsa.common.exception.CustomException;
import com.example.musinsa.dto.LowestPriceDto;
import com.example.musinsa.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Map<String, Object> getLowestPriceProductsByCategory() {
		List<LowestPriceDto> products = productRepository.findLowestPriceProductsByCategory();

		if (products.isEmpty()) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "no products found for the given categories");
		}

		if (products.size() != ProductCommonConstants.CATEGORY_COUNT) {
			throw new CustomException(HttpStatus.SERVICE_UNAVAILABLE, "category count error");
		}

		int totalPrice = products.stream().mapToInt(LowestPriceDto::getPrice).sum();

		Map<String, Object> result = new HashMap<>();
		result.put("categories", products);
		result.put("totalPrice", totalPrice);
		result.put("message", "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회.");
		return result;
	}
}
