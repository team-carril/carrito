package com.gfttraining.cart.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.User;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.exception.BadRequestParamException;
import com.gfttraining.cart.service.CartService;

@RestController
public class CartController {

	static final String[] STATUSES = { "ALL", "SUBMITTED", "DRAFT" };

	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	public List<Cart> findAllCarts() {
		return cartService.findAll();
	}

	@GetMapping("/carts")
	public List<Cart> findByStatus(@RequestParam(required = false) String status) throws BadRequestParamException {
		if (status == null)
			status = "DRAFT";
		if (status.equals("DRAFT") || status.equals("SUBMITTED"))
			return cartService.findByStatus(status);
		if (status.equals("ALL"))
			return cartService.findAll();
		throw new BadRequestParamException("Not a valid request param.");
	}

	@PostMapping("/carts")
	public Cart createCart(@RequestBody User user) throws BadRequestBodyException {
		if (user.getId() == 0)
			throw new BadRequestBodyException("Missing User id");
		return cartService.postNewCart(user);
	}

	public static boolean isValidStatus(String str) {
		for (String status : STATUSES) {
			if (status.equals(str))
				return true;
		}
		return false;
	}

}
