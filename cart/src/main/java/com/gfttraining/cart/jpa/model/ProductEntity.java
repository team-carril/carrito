package com.gfttraining.cart.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCT")
public class ProductEntity {
	@Id
	private int id;

	@Column(name = "to_cart")
	private String cartId;
	private String category;
	private String description;
	private Float price;
	private int stock;
}
