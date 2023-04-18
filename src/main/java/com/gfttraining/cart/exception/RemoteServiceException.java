package com.gfttraining.cart.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class RemoteServiceException extends IOException {
	HttpStatus status;

	public RemoteServiceException(String msg) {
		super(msg);
	}

	public RemoteServiceException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
