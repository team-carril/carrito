package com.gfttraining.cart.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
public class Product {

	@JsonIgnore
	private Integer id;
	@NotNull
	private Integer catalogId;
	@NotNull
	private String name;
	@NotNull
	private String description;
	@NotNull
	private BigDecimal price;
	private Integer quantity;

	@Builder
	static public Product create(int id, int catalogId, String name, String description, BigDecimal price,
			int quantity) {
		Product product = new Product();
		product.setId(id);
		product.setCatalogId(catalogId);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setQuantity(quantity);
		return product;
	}
}
