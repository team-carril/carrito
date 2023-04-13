package com.gfttraining.cart.api.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.exception.BadRequestParamException;
import com.gfttraining.cart.service.CartService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CartController {

	static final String[] STATUSES = { "ALL", "SUBMITTED", "DRAFT" };

	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping("/carts")
	public List<Cart> findByStatus(@RequestParam(required = false) String status) throws BadRequestParamException {
		if (status == null)
			return cartService.findByStatus("DRAFT");
		if (status.equals("DRAFT") || status.equals("SUBMITTED"))
			return cartService.findByStatus(status);
		if (status.equals("ALL"))
			return cartService.findAll();
		throw new BadRequestParamException("Not a valid request param.");
	}

	@PostMapping("/carts")
	public ResponseEntity<Cart> createCart(@Valid @RequestBody User user) throws BadRequestBodyException {
		Cart cart = cartService.postNewCart(user);

		log.info("Creating cart with id: " + cart.getId() + " and user id: "
				+ cart.getUserId());

		ResponseEntity<Cart> createCartLog = new ResponseEntity<>(cart, HttpStatus.CREATED);

		return createCartLog;
	}

	@PatchMapping("/carts/{id}")
	public Cart addProductToCart(@Valid @RequestBody ProductFromCatalog productFromCatalog, @PathVariable UUID id)
			throws BadRequestBodyException {

		Cart addProductToCartLog = cartService.addProductToCart(productFromCatalog, id);

		log.info("Product " + addProductToCartLog.getProducts() + " added");

		return addProductToCartLog;
	}

	@DeleteMapping("/carts/{id}")
	public void deleteCartById(@PathVariable UUID id) {
		cartService.deleteById(id);
		log.info("Cart" + id + "deleted");
	}

	public static boolean isValidStatus(String str) {
		for (String status : STATUSES) {
			if (status.equals(str))
				return true;
		}
		return false;
	}

	@GetMapping("/carts/users/{userId}")
	public List<Cart> getAllCartEntitiesByUserIdFilteredByStatus(@PathVariable Integer userId) {
		return cartService.getAllCartEntitiesByUserIdFilteredByStatus(userId);
	}

	@PostMapping("/carts/submit/{id}")
	public Cart validateCart(@PathVariable UUID userId) {
		return cartService.validateCart(userId);
	}
}
