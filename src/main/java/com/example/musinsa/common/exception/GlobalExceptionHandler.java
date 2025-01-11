package com.example.musinsa.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.musinsa.common.CommonResponseModel;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponseModel<Object>> handleException(CustomException ex) {
		logger.error("CustomException occurred: ", ex);

		return new ResponseEntity<>(
				new CommonResponseModel<>(ex.getStatus(), ex.getMessage(), null),
				ex.getStatus()
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponseModel<String>> handleGenericException(Exception ex) {
		logger.error("Exception occurred: ", ex);

		return new ResponseEntity<>(
				new CommonResponseModel<>(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", null),
				HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}
