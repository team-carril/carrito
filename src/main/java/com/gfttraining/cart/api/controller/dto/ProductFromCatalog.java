package com.gfttraining.cart.api.controller.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductFromCatalog {
	private int id;
	private String name;
	private BigDecimal price;
	private String description;
	private int stock;
}
