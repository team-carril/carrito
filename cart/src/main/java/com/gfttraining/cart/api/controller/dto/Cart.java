package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cart {
	
	private UUID id;
	private Integer userId;
	private Date createdAt;
	private Date updateAt;
	private String status;
	private TaxCountry taxCountry;
	private BigDecimal totalPrice;
	private List<Product> products;
}
