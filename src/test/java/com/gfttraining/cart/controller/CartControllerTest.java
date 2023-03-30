package com.gfttraining.cart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.service.CartService;

class CartControllerTest {

	@Mock
	CartService cartService;

	@InjectMocks
	CartController cartController;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void findAllCartTest() {
		cartController.findAllCarts();
		verify(cartService);
	}

}
