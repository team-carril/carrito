package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class Product {

	private Integer id;
	private String name;
	private Integer category;
	private String description;
	private BigDecimal price;
	private Integer quantity;

	private UUID cartId;

	@Builder
	static public Product create(int id, String name, String description, UUID cartId, int category, BigDecimal price,
			int quantity) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setCartId(cartId);
		product.setCategory(category);
		product.setPrice(price);
		product.setQuantity(quantity);
		return product;
	}
}
