package com.gfttraining.cart.api.controller;

import javax.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.service.ProductService;
import com.gfttraining.cart.api.dto.Product;

@RestController
public class ProductController {

	ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PatchMapping(value = "/products/{catalogId}")
	public CartCountDTO updateAllById(@Valid @RequestBody ProductFromCatalog productFromCatalog,
			@PathVariable int catalogId)
			throws BadRequestBodyException {
		return productService.updateAllById(productFromCatalog, catalogId);
	}

	@DeleteMapping(value = "/products/{catalogId}")
	public CartCountDTO deleteAllById(@PathVariable int catalogId) {
		return productService.deleteAllById(catalogId);
	}

	@GetMapping(value = "/products")
	public List<Product> findAllProductsSortedByPrice() {
		return productService.findAllProductsSortedByPrice();
	}

}
