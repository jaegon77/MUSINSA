package com.example.musinsa.repository;

import java.util.List;

import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoryForLowestPriceDto;

public interface ProductRepositoryCustom {
	List<CategoryForLowestPriceDto> findLowestPriceProductsByCategory();
	List<BrandTotalPriceDto> findBrandTotalPrices();
	List<BrandTotalPriceDto.LowestPriceDto> findProductsByBrand(String brandName);
}
