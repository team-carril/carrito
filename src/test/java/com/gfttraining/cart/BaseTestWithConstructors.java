package com.gfttraining.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

public class BaseTestWithConstructors {

	protected CartEntity cartEntity(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt,
			String status,
			List<ProductEntity> products) {
		CartEntity entity = CartEntity.builder().id(id).userId(userId).createdAt(createdAt).updatedAt(updatedAt)
				.status(status == null ? "DRAFT" : status)
				.products(products == null ? Collections.emptyList() : products).build();

		if (entity.getStatus().equals("SUBMITTED"))
			entity.setTotalPrice(entity.calculatePrice());
		return entity;
	}

	protected Cart cartDto(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt, String status,
			List<Product> products,
			double totalPrice) {
		if (id == null)
			id = UUID.randomUUID();
		if (createdAt == null)
			createdAt = LocalDateTime.now();
		if (updatedAt == null)
			updatedAt = LocalDateTime.now();
		if (products == null)
			products = Collections.emptyList();

		return Cart.builder().id(id).userId(userId).createdAt(createdAt).updatedAt(updatedAt).status(status)
				.products(products).totalPrice(BigDecimal.valueOf(totalPrice).stripTrailingZeros()).build();
	}

	protected List<CartEntity> toList(CartEntity... entities) {
		return Arrays.asList(entities);
	}

	protected List<Cart> toList(Cart... dtos) {
		return Arrays.asList(dtos);
	}

	protected ProductEntity productEntity(int id, int catalogId, String name, String description, UUID cartId,
			double price, int quantity) {
		return ProductEntity.builder().id(id).catalogId(catalogId).name(name).cartId(cartId).description(description)
				.price(new BigDecimal(price).stripTrailingZeros()).quantity(quantity).build();
	}

	protected Product productDto(int id, int catalogId, String name, String description, UUID cartId, double price,
			int quantity) {
		return Product.builder().id(id).catalogId(catalogId).name(name).description(description)
				.price(new BigDecimal(price).stripTrailingZeros()).quantity(quantity).build();
	}

	protected List<ProductEntity> toList(ProductEntity... entities) {
		return Arrays.asList(entities);
	}

	protected List<Product> toList(Product... dtos) {
		return Arrays.asList(dtos);
	}

	protected List<ProductFromCatalog> toList(ProductFromCatalog... dtos) {
		return Arrays.asList(dtos);
	}

	protected User userDTO(int id) {
		User user = new User();
		user.setId(id);

		return user;
	}

	protected ProductFromCatalog productFromCatalog(int id, String name, String description, double price,
			int quantity) {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(id);
		product.setName(name);
		product.setDescription(description == null ? "" : description);
		product.setPrice(new BigDecimal(price).stripTrailingZeros());
		product.setQuantity(quantity);
		return product;
	}

	protected ProductFromCatalog productFromCatalog(int id, String name, String description, double price) {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(id);
		product.setName(name);
		product.setPrice(new BigDecimal(price).stripTrailingZeros());
		product.setDescription(description == null ? "" : description);
		return product;
	}

	protected ProductFromCatalog productFromCatalog(int id, double price, int stock) {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(id);
		product.setPrice(new BigDecimal(price).stripTrailingZeros());
		product.setStock(stock);
		return product;
	}
}
