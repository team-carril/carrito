package com.gfttraining.cart.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.service.CartService;

@RestController
public class CartController {

	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping("/carts")
	public List<Cart> findAllCarts() {
		return cartService.findAll();
	}

}
