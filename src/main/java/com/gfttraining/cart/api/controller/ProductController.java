package com.gfttraining.cart.api.controller;

import java.nio.channels.UnsupportedAddressTypeException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@PatchMapping(value = "/products/{id}")
	public void updateAllById(@PathVariable int id) {
		throw new UnsupportedOperationException();
	}

	@DeleteMapping(value = "/products/{id}")
	public void deleteAllById(@PathVariable int id) {
		throw new UnsupportedOperationException();
	}

}
