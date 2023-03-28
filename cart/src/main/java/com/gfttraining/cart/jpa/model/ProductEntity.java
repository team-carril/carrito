package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.Product;

import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCT")
public class ProductEntity {
	@Id
	private int id;

	private String name;

	@Column(name = "to_cart")
	private String cartId;
	private Integer category;
	private String description;
	@Column(precision = 10, scale = 4)
	private BigDecimal price;
	private int quantity;

	public BigDecimal getTotalPrize()
	{
		return this.getPrice().multiply(BigDecimal.valueOf(this.getQuantity()));
	}

	static public Product toDTO(ProductEntity entity)
	{
		return Product.builder()
		.id(entity.getId())
		.name(entity.getName())
		.category(entity.getCategory())
		.description(entity.getDescription())
		.price(entity.getPrice())
		.quantity(entity.getQuantity())
		.build();
	}
}
