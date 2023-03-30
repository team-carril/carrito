package com.gfttraining.cart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.gfttraining.cart.api.controller.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestParamException.class)
	public ResponseEntity<ErrorResponse> handlesBadRequestParamException(BadRequestParamException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadRequestBodyException.class)
	public ResponseEntity<ErrorResponse> handlesBadRequestBodyException(BadRequestBodyException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}

}
