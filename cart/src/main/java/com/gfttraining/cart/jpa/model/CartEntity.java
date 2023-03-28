package com.gfttraining.cart.jpa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.TaxCountry;

import lombok.Data;

@Entity
@Data
@Table(name = "CART")
public class CartEntity {

	private String id; 
	private String userId; 

	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "updated_at")
	private Date updatedAt;
	private String status;

	@OneToMany
	@JoinColumn(name = "to_cart", referencedColumnName = "id")
	private List<ProductEntity> products;
	
	@ManyToOne
	@JoinColumn(name = "to_tax_country", referencedColumnName = "id")
	private TaxCountry taxCountry;
}
