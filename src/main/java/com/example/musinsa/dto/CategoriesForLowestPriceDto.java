package com.example.musinsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesForLowestPriceDto {
	private String category;
	private String brandName;
	private int price;
}
