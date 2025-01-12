package com.example.musinsa.dto;

import com.example.musinsa.common.util.OperationType;

import lombok.Data;

@Data
public class ProductRequest {
	private Long id; // 업데이트/삭제 시 사용
	private String name;
	private int price;
	private Long brandId;
	private Long categoryId;
	private OperationType operation; // ADD, UPDATE, DELETE
}
