package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

	private Integer id;
	private String name;
	private Integer category;
	private String description;
	private BigDecimal price;
	private Integer quantity;
}
