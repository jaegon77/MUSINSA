package com.example.musinsa.dto;

import java.util.List;

import com.example.musinsa.common.util.Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandTotalPriceDto {
	private String brandName;
	private int totalPrice;
	private List<LowestPriceDto> lowestPriceDto;

	public BrandTotalPriceDto(String brandName, int totalPrice) {
		this.brandName = brandName;
		this.totalPrice = totalPrice;
	}

	@Getter
	@Setter
	public static class LowestPriceDto {
		private String category;
		private int price;

		public LowestPriceDto(String category, int price) {
			this.category = category;
			this.price = price;
		}

		@JsonProperty("price")
		public String getFormattedPrice() {
			return Util.PriceFormatterUtil(price);
		}
	}
}
