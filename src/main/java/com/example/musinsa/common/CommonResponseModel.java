package com.example.musinsa.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponseModel<T> {
	private HttpStatus status;
	private String message;
	private T data;
}
