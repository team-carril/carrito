package com.gfttraining.cart.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.config.FeatureConfiguration;
import com.gfttraining.cart.exception.BadMethodRequestException;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.service.ProductService;

public class ProductControllerTest extends BaseTestWithConstructors {

	@Mock
	ProductService productService;

	@Mock
	FeatureConfiguration featureConfiguration;

	@InjectMocks
	ProductController productController;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void updateAll_calls_service() throws BadRequestBodyException, BadMethodRequestException {
		ProductFromCatalog productFromCatalog = new ProductFromCatalog();
		productFromCatalog.setPrice(new BigDecimal(7));
		when(featureConfiguration.getUpdateAllByIdEnabled()).thenReturn(true);
		productController.updateAllById(productFromCatalog, 1);
		verify(productService).updateAllById(any(ProductFromCatalog.class), anyInt());
	}

	@Test
	public void deleteAll_calls_service() {
		productController.deleteAllById(1);
		verify(productService).deleteAllById(1);
	}

	@Test
	public void findAllProductsSortedByPrice() {
		productController.findAllProductsSortedByPrice();
		verify(productService).findAllProductsSortedByPrice();
	}

	@Test
	public void updateAll_calls_service_false() throws BadRequestBodyException, BadMethodRequestException {
		ProductFromCatalog productFromCatalog = new ProductFromCatalog();
		productFromCatalog.setPrice(new BigDecimal(7));
		when(featureConfiguration.getUpdateAllByIdEnabled()).thenReturn(false);
		assertThrows(BadMethodRequestException.class, () -> productController.updateAllById(productFromCatalog, 1));
	}

}
