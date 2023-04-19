package com.gfttraining.cart.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class RemoteServiceInternalException extends IOException {
	HttpStatus status;

	public RemoteServiceInternalException(String msg) {
		super(msg);
	}

	public RemoteServiceInternalException(String msg, HttpStatus status) {
		super(msg);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
