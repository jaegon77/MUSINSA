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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/category/lowestPrice")
	public Object getLowestPriceProductsByCategory() {
		return productService.getLowestPriceProductsByCategory();
	}

	@GetMapping("/brand/lowestPrice")
	public Object getLowestPriceProductsByBrand() {
		return productService.getLowestPriceProductsByBrand();
	}

	@GetMapping("/category/highestLowest")
	public Object getHighestAndLowestPriceByCategory(@RequestParam String categoryName) {
		return productService.getHighestAndLowestPriceByCategory(categoryName);
	}

	@PostMapping("/brand")
	public Object handleBrandOperation(@RequestBody BrandRequest request) {
		return productService.handleBrandOperation(request);
	}

	@PostMapping("/category")
	public Object handleCategoryOperation(@RequestBody CategoryRequest request) {
		return productService.handleCategoryOperation(request);
	}

	@PostMapping("/product")
	public Object handleProductOperation(@RequestBody ProductRequest request) {
		return productService.handleProductOperation(request);
	}
}
