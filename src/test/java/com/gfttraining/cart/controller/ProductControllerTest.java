package com.gfttraining.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.service.ProductService;

public class ProductControllerTest extends BaseTestWithConstructors {

	@Mock
	ProductService productService;

	@InjectMocks
	ProductController productController;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void updateAll_calls_service() throws BadRequestBodyException {
		ProductFromCatalog productFromCatalog = new ProductFromCatalog();
		productFromCatalog.setPrice(new BigDecimal(7));
		productController.updateAllById(productFromCatalog, 1);
		verify(productService).updateAllById(any(Product.class), anyInt());
	}

	@Test
	public void deleteAll_calls_service() {
		productController.deleteAllById(1);
		verify(productService).deleteAllById(1);
	}

}
