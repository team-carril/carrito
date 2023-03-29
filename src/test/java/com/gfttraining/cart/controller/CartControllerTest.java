package com.gfttraining.cart.controller;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.service.CartService;

class CartControllerTest extends BaseTestWithConstructors{
	
	@Mock
	CartService cartService;
	
	@InjectMocks
	CartController cartController;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void FindAllCarttest() {
		cartController.findAllCarts();
		verify(cartService);
	}
}
