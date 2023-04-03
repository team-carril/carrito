package com.gfttraining.cart.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWarDeployment;

import lombok.Data;

@Data
public class ProductFromCatalog {
	@Min(value = 1, message = "Id is required and positive.")
	private int id;
	@NotEmpty
	private String name;
	@NotNull
	private BigDecimal price;
	@NotNull
	private String description;
	private int stock;
}
