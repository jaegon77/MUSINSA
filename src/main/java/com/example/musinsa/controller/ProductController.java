package com.example.musinsa.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
