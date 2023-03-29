package com.gfttraining.cart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;

@Service
public class CartService {

	private CartRepository cartRepository;

	public CartService(CartRepository cartRepository) {this.cartRepository = cartRepository;}

	public List<Cart> findAll() {
		List<CartEntity> cartEntityList = cartRepository.findAll();
		return cartEntityList.stream().map(CartEntity::toDTO).collect(Collectors.toList());
	}
}
