package com.gfttraining.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.Product;
import com.gfttraining.cart.api.controller.dto.TaxCountry;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;
import com.gfttraining.cart.jpa.model.TaxCountryEntity;

public class BaseTestWithConstructors {

	protected TaxCountryEntity taxCountryEntity(String name, int taxRate) {
		return TaxCountryEntity.create(name, taxRate);
	}

	protected TaxCountry taxCountry(String name, int taxRate) {
		return TaxCountry.create(name, taxRate);
	}

	protected CartEntity cartEntity(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt,
			String status,
			List<ProductEntity> products, TaxCountryEntity taxCountry) {
		return CartEntity.builder().id(id).userId(userId).createdAt(createdAt).updatedAt(updatedAt).status(status)
				.products(products).taxCountry(taxCountry).build();

	}

	protected Cart cartDto(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt, String status,
			List<Product> products,
			TaxCountry taxCountry, double totalPrice) {

		return Cart.builder().id(id).userId(userId).createdAt(createdAt).updatedAt(updatedAt).status(status)
				.products(products).taxCountry(taxCountry).totalPrice(BigDecimal.valueOf(totalPrice).stripTrailingZeros()).build();
	}

	protected List<CartEntity> toList(CartEntity... entities) {
		return Arrays.asList(entities);
	}

	protected List<Cart> toList(Cart... dtos) {
		return Arrays.asList(dtos);
	}

	protected ProductEntity productEntity(int id, String name, String description, UUID cartId,
			double price, int quantity) {
		return ProductEntity.builder().id(id).name(name).cartId(cartId).description(description)
				.price(BigDecimal.valueOf(price)).quantity(quantity).build();
	}

	protected Product productDto(int id, String name, String description, UUID cartId, double price,
			int quantity) {
		return Product.builder().id(id).name(name).description(description)
				.price(BigDecimal.valueOf(price)).quantity(quantity).cartId(cartId).build();
	}

	protected List<ProductEntity> toList(ProductEntity... entities) {
		return Arrays.asList(entities);
	}

	protected List<Product> toList(Product... dtos) {
		return Arrays.asList(dtos);
	}
}
