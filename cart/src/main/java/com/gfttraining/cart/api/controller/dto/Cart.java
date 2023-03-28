package com.gfttraining.cart.api.controller.dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cart {
	
	private String id;
	private Integer userId;
	private Date createdAt;
	private Date updateAt;
	private String status;
	private TaxCountry taxCountry;
	private Float totalPrice;
	private List<Product> products;
}
