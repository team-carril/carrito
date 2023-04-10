package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gfttraining.cart.exception.NegativeQuantityException;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCT")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "catalog_id")
	private int catalogId;

	private String name;
	@Column(name = "to_cart")
	private UUID cartId;
	private String description;
	@Column(precision = 10, scale = 4)
	private BigDecimal price;
	private int quantity;

	public BigDecimal getTotalPrize() {
		return this.getPrice().multiply(BigDecimal.valueOf(this.getQuantity()));
	}

	public void addToQuantity(int x) throws NegativeQuantityException {
		if ((this.quantity + x) < 0)
			throw new NegativeQuantityException(
					"Quantity must result in a positive integer. Current quantity: " + this.quantity);
		this.quantity += x;
	}

	@Builder
	static public ProductEntity create(int id, int catalogId, String name, String description, UUID cartId,
			BigDecimal price, int quantity) {
		ProductEntity entity = new ProductEntity();
		entity.setId(id);
		entity.setCatalogId(catalogId);
		entity.setName(name);
		entity.setCartId(cartId);
		entity.setDescription(description);
		entity.setPrice(price);
		entity.setQuantity(quantity);
		return entity;
	}
}
