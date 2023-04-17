package com.gfttraining.cart.api.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Min(value = 1, message = "Id is required and positive.")
	private int id;
	private String email;
	private String name;
	private String address;
	private String paymentMethod;
	private String country;
}
