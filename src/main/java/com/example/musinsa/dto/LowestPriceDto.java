package com.example.musinsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowestPriceDto {
	private String category;
	private String brandName;
	private Integer price;
}
