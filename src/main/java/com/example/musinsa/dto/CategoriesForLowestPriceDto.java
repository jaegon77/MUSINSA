package com.example.musinsa.dto;

import com.example.musinsa.common.util.Util;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty("price")
	public String getFormattedPrice() {
		return Util.PriceFormatterUtil(price);
	}
}
