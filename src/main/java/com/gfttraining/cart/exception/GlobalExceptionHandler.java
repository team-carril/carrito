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

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestParamException.class)
	public ResponseEntity<ErrorResponse> handlesBadRequestParamException(BadRequestParamException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		log.error("Message: " + ex.getMessage() + " Cause: " + ex.getCause() + " Stack Trace: " + ex.getStackTrace()
				+ " Localized Message: " + ex.getLocalizedMessage());

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlesBadRequestBodyException(EntityNotFoundException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		log.error("Message: " + ex.getMessage() + " Cause: " + ex.getCause() + " Stack Trace: " + ex.getStackTrace()
				+ " Localized Message: " + ex.getLocalizedMessage());

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handlesBadPathVariableException(MethodArgumentTypeMismatchException ex,
			WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		log.error("Message: " + ex.getMessage() + " Cause: " + ex.getCause() + " Stack Trace: " + ex.getStackTrace()
				+ " Localized Message: " + ex.getLocalizedMessage());

		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}

	@Generated
	@ExceptionHandler(ImpossibleQuantityException.class)
	public ResponseEntity<ErrorResponse> handleNegativeQuantityException(ImpossibleQuantityException ex,
			WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();
		log.error("Invalid quantity for a product. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<ErrorResponse>(res, HttpStatus.CONFLICT);
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

		log.error("Message: " + ex.getMessage() + " Cause: " + ex.getCause() + " Stack Trace: " + ex.getStackTrace()
				+ " Localized Message: " + ex.getLocalizedMessage());

		return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RemoteServiceInternalException.class)
	public ResponseEntity<ErrorResponse> handleRemoteServiceInternalException(RemoteServiceInternalException ex,
			WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.msg(ex.getMessage())
				.build();
		log.error("Remote service internal error. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@Generated
	public ResponseEntity<ErrorResponse> handleRemoteServiceBadRequestException(RemoteServiceBadRequestException ex,
			WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.msg(ex.getMessage())
				.build();

		log.error("Remote service conflict. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(OutOfStockException.class)
	public ResponseEntity<ErrorResponse> handleOutOfstockException(OutOfStockException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.msg(ex.getMessage())
				.build();
		log.error("Catalog external service out of stock. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidUserDataException.class)
	public ResponseEntity<ErrorResponse> handleInvalidUserDataExecption(InvalidUserDataException ex, WebRequest req) {
		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now())
				.msg(ex.getMessage())
				.build();

		log.error("Invalid user payment data. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}

	@Generated
	@ExceptionHandler(BadMethodRequestException.class)
	public ResponseEntity<ErrorResponse> handlerInvalidMethod(BadMethodRequestException ex,
			WebRequest req) {

		ErrorResponse res = ErrorResponse.builder().timestamp(LocalDateTime.now()).msg(ex.getMessage()).build();

		log.error("Feature is disabled. {} | {}", res.getMsg(), ex.getCause());
		return new ResponseEntity<ErrorResponse>(res, HttpStatus.METHOD_NOT_ALLOWED);
	}

}
