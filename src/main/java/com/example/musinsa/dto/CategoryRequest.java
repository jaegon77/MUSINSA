package com.example.musinsa.dto;

import com.example.musinsa.common.util.OperationType;

import lombok.Data;

@Data
public class CategoryRequest {
	private Long id;
	private String name;
	private OperationType operation;
}
