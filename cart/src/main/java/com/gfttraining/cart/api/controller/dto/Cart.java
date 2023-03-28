package com.gfttraining.cart.api.controller.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Cart {
	
	private String id;
	private Integer userId;
	private Date createdAt;
	private Date updateAt;
	private String status;
	private TaxCountry taxCounty;
	private Float totalPrice;
	private List<ProductPair> productPair;
}
