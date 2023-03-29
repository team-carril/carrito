package com.gfttraining.cart.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.Product;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;
import com.gfttraining.cart.service.CartService;

public class CartServiceTest extends BaseTestWithConstructors {

	@Mock
	CartRepository cartRepository;

	@InjectMocks
	CartService cartService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void findAll_calls_repository() {
		cartService.findAll();
		verify(cartRepository).findAll();
	}

	@Test public void
	returns_valid_dto()
	{
		Date testDate = new Date(777);
		UUID uuidA = UUID.randomUUID();
		UUID uuidB = UUID.randomUUID();
		UUID uuidC = UUID.randomUUID();

		List<ProductEntity> p1 = toList(
			productEntity(1, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productEntity(2, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productEntity(3, "test_item", "asdf", uuidA, 1, 5.0, 1)
		) ;
		List<Product> p2 = toList(
			productDto(1, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productDto(2, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productDto(3, "test_item", "asdf", uuidA, 1, 5.0, 1)
		);

		List<CartEntity> listInput = toList(
			cartEntity(uuidA, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0)),
			cartEntity(uuidB, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0)),
			cartEntity(uuidC, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0))
		);
		List<Cart> listExpected = toList(
			cartDto(uuidA, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0),
			cartDto(uuidB, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0),
			cartDto(uuidC, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0)
		);
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();

		assertEquals(listExpected, listActual);
	}

	@Test
	public void handles_empty_product_list() {
		Date testDate = new Date(777);
		UUID uuidA = UUID.randomUUID();


		List<CartEntity> listInput = toList(
				cartEntity(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(),
						taxCountryEntity("SPAIN", 21)));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(), taxCountry("SPAIN", 21),
						0.0));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);
	}
}
