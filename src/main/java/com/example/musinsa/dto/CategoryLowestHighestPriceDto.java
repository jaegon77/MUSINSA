package com.example.musinsa.dto;

import com.example.musinsa.common.util.Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryLowestHighestPriceDto {
	private String category;
	private LowestPriceByCategory lowestPriceByCategory;
	private HighestPriceByCategory highestPriceByCategory;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LowestPriceByCategory {
		private String brandName;
		private int price;

		@JsonProperty("price")
		public String getFormattedPrice() {
			return Util.PriceFormatterUtil(price);
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class HighestPriceByCategory {
		private String brandName;
		private int price;

		@JsonProperty("price")
		public String getFormattedPrice() {
			return Util.PriceFormatterUtil(price);
		}
	}
}
