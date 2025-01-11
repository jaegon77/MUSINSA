package com.example.musinsa.repository;

import java.util.List;

import com.example.musinsa.dto.BrandTotalPriceDto;
import com.example.musinsa.dto.CategoriesForLowestPriceDto;
import com.example.musinsa.dto.CategoryLowestHighestPriceDto;

public interface ProductRepositoryCustom {
	List<CategoriesForLowestPriceDto> findLowestPriceProductsByCategory();
	List<BrandTotalPriceDto> findBrandTotalPrices();
	List<BrandTotalPriceDto.LowestPriceDto> findProductsByBrand(String brandName);
	CategoryLowestHighestPriceDto.LowestPriceByCategory findLowestPriceByCategory(String categoryName);
	CategoryLowestHighestPriceDto.HighestPriceByCategory findHighestPriceByCategory(String categoryName);
}
