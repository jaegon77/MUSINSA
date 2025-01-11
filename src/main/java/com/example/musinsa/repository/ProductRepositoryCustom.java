package com.example.musinsa.repository;

import java.util.List;

import com.example.musinsa.dto.LowestPriceDto;

public interface ProductRepositoryCustom {
	List<LowestPriceDto> findLowestPriceProductsByCategory();
}
