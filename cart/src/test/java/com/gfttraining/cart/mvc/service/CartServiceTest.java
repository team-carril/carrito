package com.gfttraining.cart.mvc.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.service.CartService;

public class CartServiceTest {

	@Mock
	CartRepository cartRepository;

	@InjectMocks
	CartService cartService;

	@BeforeEach
	public void init ()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test public void 
	findAll_calls_repository()
	{
		cartService.findAll();
		verify(cartRepository).findAll();
	}


	
}
