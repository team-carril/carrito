package com.gfttraining.cart.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class Cart {

	private UUID id;
	private Integer userId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String status;
	private BigDecimal totalPrice;
	private List<Product> products;

	@Builder
	public static Cart create(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt, String status,
			List<Product> products, BigDecimal totalPrice) {
		Cart cart = new Cart();
		cart.setId(id);
		cart.setUserId(userId);
		cart.setCreatedAt(createdAt);
		cart.setUpdatedAt(updatedAt);
		cart.setStatus(status);
		cart.setProducts(products);
		cart.setTotalPrice(totalPrice);
		return cart;
	}
}
