package com.example.musinsa.dto;

import com.example.musinsa.common.util.OperationType;

import lombok.Data;

@Data
public class BrandRequest {
	private Long id;
	private String name;
	private OperationType operation;
}
