package com.gfttraining.cart.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;

public class RemoteServiceInternalException extends IOException {
	HttpStatus status;

	public RemoteServiceInternalException(String msg) {
		super(msg);
	}
}
