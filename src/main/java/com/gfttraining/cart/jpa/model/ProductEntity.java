package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.Product;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCT")
public class ProductEntity {
	@Id
	private int id;

	private String name;

	@Column(name = "to_cart")
	private UUID cartId;
	private Integer category;
	private String description;
	@Column(precision = 10, scale = 4)
	private BigDecimal price;
	private int quantity;

	public BigDecimal getTotalPrize() {
		return this.getPrice().multiply(BigDecimal.valueOf(this.getQuantity()));
	}

	@Builder
	static public ProductEntity create(int id, String name, String description, UUID cartId, int category,
			BigDecimal price, int quantity) {
		ProductEntity entity = new ProductEntity();
		entity.setId(id);
		entity.setName(name);
		entity.setCartId(cartId);
		entity.setDescription(description);
		entity.setCategory(category);
		entity.setPrice(price);
		entity.setQuantity(quantity);
		entity.setCartId(cartId);
		return entity;
	}

	static public Product toDTO(ProductEntity entity) {
		return Product.builder().id(entity.getId()).name(entity.getName()).category(entity.getCategory())
				.description(entity.getDescription()).price(entity.getPrice()).cartId(entity.getCartId())
				.quantity(entity.getQuantity()).build();
	}
}
