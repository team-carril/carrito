package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class Cart {
	
	private UUID id;
	private Integer userId;
	private Date createdAt;
	private Date updatedAt;
	private String status;
	private TaxCountry taxCountry;
	private BigDecimal totalPrice;
	private List<Product> products;

	@Builder
	public static Cart create(UUID id, int userId, Date createdAt,
			Date updatedAt, String status, List<Product> products, TaxCountry taxCountry, BigDecimal totalPrice) 
	{
		Cart cart = new Cart();
		cart.setId(id);
		cart.setUserId(userId);
		cart.setCreatedAt(createdAt);
		cart.setUpdatedAt(updatedAt);
		cart.setStatus(status);
		cart.setProducts(products);
		cart.setTaxCountry(taxCountry);
		cart.setTotalPrice(totalPrice);
		return cart;
	}
}
