package com.gfttraining.cart.controller;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.service.ProductService;

public class ProductControllerTest {

	@Mock
	ProductService productService;

	@InjectMocks
	ProductController productController;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void updateAll_calls_service() {
		productController.updateAllById(1);
		verify(productService).updateAllById(1);
	}

	@Test
	public void deleteAll_calls_service() {
		productController.deleteAllById(1);
		verify(productService).deleteAllById(1);
	}

}
