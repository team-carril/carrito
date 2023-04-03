package com.gfttraining.cart.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.jpa.ProductRepository;

public class ProductServiceTest extends BaseTestWithConstructors {

	@Mock
	ProductRepository productRepository;
	@InjectMocks
	ProductService service;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void updateAll_calls_service() {
		service.updateAllById(1);
		verify(productRepository).findByCatalogId(1);
	}

	@Test
	public void deleteAll_calls_service() {
		service.deleteAllById(1);
		verify(productRepository).findByCatalogId(1);
	}

}
