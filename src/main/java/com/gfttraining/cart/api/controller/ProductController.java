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
import com.gfttraining.cart.config.FeatureConfiguration;
import com.gfttraining.cart.exception.BadMethodRequestException;
import com.gfttraining.cart.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import com.gfttraining.cart.api.dto.Product;

@RestController
@Slf4j
public class ProductController {

	ProductService productService;
	FeatureConfiguration featureConfiguration;

	public ProductController(ProductService productService, FeatureConfiguration featureConfiguration) {
		this.productService = productService;
		this.featureConfiguration = featureConfiguration;
	}

	@PatchMapping(value = "/products/{catalogId}")
	public CartCountDTO updateAllById(@Valid @RequestBody ProductFromCatalog productFromCatalog,
			@PathVariable int catalogId)
			throws BadMethodRequestException {

		if (!featureConfiguration.getUpdateAllByIdEnabled()) {
			throw new BadMethodRequestException("Feature Flag is Disable");
		}

		log.info("Updating every instance of product {}", catalogId);
		CartCountDTO count = productService.updateAllById(productFromCatalog, catalogId);
		log.info("Operation success. Carts affected: {}", count.getCartsChanged());
		return count;
	}

	@DeleteMapping(value = "/products/{catalogId}")
	public CartCountDTO deleteAllById(@PathVariable int catalogId) {
		log.info("Deleting every instance of product {}", catalogId);
		CartCountDTO count = productService.deleteAllById(catalogId);
		log.info("Operation success. Carts affected: {}", count.getCartsChanged());
		return count;
	}

	@GetMapping(value = "/products")
	public List<Product> findAllProductsSortedByPrice() {
		return productService.findAllProductsSortedByPrice();
	}

}
