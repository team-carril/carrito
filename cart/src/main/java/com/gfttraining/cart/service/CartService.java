package com.gfttraining.cart.service;

import java.util.List;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;

public class CartService {
	
	private CartRepository cartRepository;
	

	public List<Cart> findAll()
	{
		List<CartEntity> cartEntityList = cartRepository.findAll();
		List<Cart> cartList;
		
		
	}
}
