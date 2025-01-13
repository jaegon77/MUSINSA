package com.example.musinsa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsa.dto.BrandRequest;
import com.example.musinsa.dto.CategoryRequest;
import com.example.musinsa.dto.ProductRequest;
import com.example.musinsa.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "상품 관리 API")
public class ProductController {
	private final ProductService productService;

	@Operation(summary = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회", description = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회합니다.")
	@GetMapping("/category/lowestPrice")
	public Object getLowestPriceProductsByCategory() {
		return productService.getLowestPriceProductsByCategory();
	}

	@Operation(summary = "단일 브랜드 모든 카테고리 상품 합 최저가 카테고리의 상품가격, 총액을 조회", description = "단일 브랜드 모든 카테고리 상품 합 최저가 카테고리의 상품가격, 총액을 조회합니다.")
	@GetMapping("/brand/lowestPrice")
	public Object getLowestPriceProductsByBrand() {
		return productService.getLowestPriceProductsByBrand();
	}

	@Operation(summary = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회", description = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회합니다.")
	@GetMapping("/category/highestLowest")
	public Object getHighestAndLowestPriceByCategory(@RequestParam String categoryName) {
		return productService.getHighestAndLowestPriceByCategory(categoryName);
	}

	@Operation(summary = "브랜드 관리", description = "브랜드 추가, 업데이트, 삭제를 수행합니다.")
	@PostMapping("/brand")
	public Object handleBrandOperation(@RequestBody BrandRequest request) {
		return productService.handleBrandOperation(request);
	}

	@Operation(summary = "카테고리 관리", description = "카테고리 추가, 업데이트, 삭제를 수행합니다.")
	@PostMapping("/category")
	public Object handleCategoryOperation(@RequestBody CategoryRequest request) {
		return productService.handleCategoryOperation(request);
	}

	@Operation(summary = "상품 관리", description = "상품 추가, 업데이트, 삭제를 수행합니다.")
	@PostMapping("/product")
	public Object handleProductOperation(@RequestBody ProductRequest request) {
		return productService.handleProductOperation(request);
	}
}
