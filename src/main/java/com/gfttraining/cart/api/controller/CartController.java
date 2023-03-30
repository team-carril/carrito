package com.gfttraining.cart.api.controller;

import java.util.List;

import org.hibernate.stat.internal.StatsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.controller.dto.Cart;
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

	public static boolean isValidStatus(String str) {
		for (String status : STATUSES) {
			if (status.equals(str))
				return true;
		}
		return false;
	}

}
