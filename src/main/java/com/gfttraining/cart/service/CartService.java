package com.gfttraining.cart.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.User;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;

@Service
public class CartService {

	private CartRepository cartRepository;

	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public List<Cart> findAll() {
		List<CartEntity> cartEntityList = cartRepository.findAll();
		return cartEntityList.stream().map(CartEntity::toDTO)
				.sorted(Comparator.comparing(Cart::getStatus)).collect(Collectors.toList());
	}

	public List<Cart> findByStatus(String status) {
		List<CartEntity> entities = cartRepository.findByStatus(status);
		return entities.stream().map(CartEntity::toDTO).collect(Collectors.toList());
	}

	public Cart postNewCart(User user) {
		CartEntity entity = CartEntity.builder().createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.userId(user.getId())
				.status("DRAFT").products(Collections.emptyList()).build();
		CartEntity result = cartRepository.save(entity);
		return CartEntity.toDTO(result);
	}
}
