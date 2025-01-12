package com.example.musinsa.common.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.musinsa.common.CommonResponseModel;
import com.example.musinsa.common.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ResponseFormatAspect {

	@Around("execution(* com.example.musinsa.controller..*(..))")
	public Object formatResponse(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			Object result = joinPoint.proceed();
			String message = "Operation successful";

			if (result instanceof Map) {
				Map<String, Object> response = (Map<String, Object>) result;
				message = response.containsKey("message") ? response.get("message").toString() : "Operation successful";
				if (response.containsKey("message")) {
					response.remove("message");
				}
			}

			return new CommonResponseModel<>(HttpStatus.OK, message, result);
		} catch (CustomException ex) {
			return new CommonResponseModel<>(ex.getStatus(), ex.getMessage(), null);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return new CommonResponseModel<>(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
		}
	}
}
