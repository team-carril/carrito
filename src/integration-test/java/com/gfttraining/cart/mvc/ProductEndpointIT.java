package com.gfttraining.cart.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductEndpointIT {

	ProductService productService;

	@Test
	public void PATCH_products_OK() {
		return;
	}

	@Test
	public void PATCH_products_NOT_FOUND() {
		return;
	}

	@Test
	public void PATCH_products_BAD_REQUEST_BODY() {
		return;
	}

	@Test
	public void PATCH_products_BAD_REQUEST_PATH_VARIABLE() {
		return;
	}

	@Test
	public void DELETE_products_OK() {
		return;
	}

	@Test
	public void DELETE_products_NOT_FOUND() {
		return;
	}

	@Test
	public void DELETE_products_BAD_REQUEST_PATH_VARIABLE() {
		return;
	}

}
