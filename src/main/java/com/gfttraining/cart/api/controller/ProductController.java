package com.gfttraining.cart.api.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.service.ProductService;

@RestController
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// TODO RETURN COUNT OF ITEMS FOUND
	// TODO CHECK FOR STATUS OF CART: in JPA Query???
	@PatchMapping(value = "/products/{id}")
	public void updateAllById(@Valid @RequestBody ProductFromCatalog productFromCatalog, @PathVariable int id)
			throws BadRequestBodyException {
		Product product = Product.fromCatalog(productFromCatalog);
		productService.updateAllById(product, id);
	}

	@DeleteMapping(value = "/products/{id}")
	public void deleteAllById(@PathVariable int id) {
		productService.deleteAllById(id);
	}

}
