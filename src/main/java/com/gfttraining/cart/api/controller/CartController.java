package com.gfttraining.cart.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
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

	@PatchMapping("/carts/{id}")
	public Cart addProductToCart(@RequestBody ProductFromCatalog productFromCatalog, @PathVariable UUID id)
			throws BadRequestBodyException {
		Product product = Product.fromCatalog(productFromCatalog, id);
		return cartService.addProductToCart(product, id);
	}

	@DeleteMapping("/carts/{id}")
	public void deleteCartById(@PathVariable UUID id) {
		cartService.deleteById(id);
	}

	public static boolean isValidStatus(String str) {
		for (String status : STATUSES) {
			if (status.equals(str))
				return true;
		}
		return false;
	}

}
