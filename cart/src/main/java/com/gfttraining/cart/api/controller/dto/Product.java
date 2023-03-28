package com.gfttraining.cart.api.controller.dto;

import lombok.Data;

@Data
public class Product {

	private Integer id;
	private String name;
	private Integer category;
	private String description;
	private Float price;
	private Integer stock;
	
}
