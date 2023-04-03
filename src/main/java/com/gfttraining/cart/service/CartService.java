package com.gfttraining.cart.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

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

	public Cart addProductToCart(Product product, UUID cartId) {
		Optional<CartEntity> entityOptional = cartRepository.findById(cartId);
		if (entityOptional.isEmpty())
			throw new EntityNotFoundException("Cart " + cartId + " not found.");
		CartEntity entity = entityOptional.get();

		Optional<ProductEntity> sameProduct = entity.getProducts().stream()
				.filter(p -> (p.getCatalogId() == product.getCatalogId() && p.getCartId().equals(cartId)))
				.findFirst();
		if (sameProduct.isEmpty()) {
			// ProductEntity
			entity.getProducts().add(ProductEntity.fromDTO(product));
			entity = cartRepository.saveAndFlush(entity);
			return CartEntity.toDTO(entity);
		}
		sameProduct.get().addOne();
		entity = cartRepository.saveAndFlush(entity);
		return CartEntity.toDTO(entity);
	}

	public Cart deleteById(UUID cartId) {
		Optional<CartEntity> entityOptional = cartRepository.findById(cartId);
		if (entityOptional.isEmpty())
			throw new EntityNotFoundException("Cart " + cartId + " not found.");
		cartRepository.delete(entityOptional.get());
		return new Cart();
	}
}
