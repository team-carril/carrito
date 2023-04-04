package com.gfttraining.cart.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.gfttraining.cart.api.dto.ErrorResponse;

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

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlesBadRequestBodyException(EntityNotFoundException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handlesBadPathVariableException(MethodArgumentTypeMismatchException ex,
			WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex,
			WebRequest req) {
		Map<String, Object> fieldErrors = new HashMap<>();
		fieldErrors.put("timestamp", LocalDateTime.now());

		Map<String, String> errors = ex.getBindingResult().getFieldErrors()
				.stream()
				.reduce(new HashMap<String, String>(),
						(m, err) -> {
							m.put(err.getField(), err.getDefaultMessage());
							return m;
						},
						(m1, m2) -> m1);

		fieldErrors.put("errorCount", ex.getErrorCount());
		fieldErrors.put("errors", errors);

		return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
	}

}
