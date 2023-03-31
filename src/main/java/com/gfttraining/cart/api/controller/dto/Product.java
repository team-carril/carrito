package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.gfttraining.cart.exception.BadRequestBodyException;

import lombok.Builder;
import lombok.Data;

@Data
public class Product {

	private Integer id;
	private Integer catalogId;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer quantity;

	private UUID cartId;

	@Builder
	static public Product create(int id, int catalogId, String name, String description, UUID cartId, BigDecimal price,
			int quantity) {
		Product product = new Product();
		product.setId(id);
		product.setCatalogId(catalogId);
		product.setName(name);
		product.setCartId(cartId);
		product.setPrice(price);
		product.setQuantity(quantity);
		return product;
	}

	static public Product fromCatalog(ProductFromCatalog productFromCatalog, UUID cartId)
			throws BadRequestBodyException {
		if (productFromCatalog.getId() == 0 || productFromCatalog.getName() == null
				|| productFromCatalog.getPrice() == null)
			throw new BadRequestBodyException("Wrong Product JSON.");
		return Product.builder().catalogId(productFromCatalog.getId())
				.name(productFromCatalog.getName())
				.cartId(cartId)
				.description(productFromCatalog.getDescription())
				.price(productFromCatalog.getPrice())
				.quantity(1)
				.build();
	}

}
