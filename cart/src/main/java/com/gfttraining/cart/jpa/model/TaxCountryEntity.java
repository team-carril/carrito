package com.gfttraining.cart.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "TAXCOUNTRY")
@Data
public class TaxCountryEntity {
	@Id
	@GeneratedValue
	private	int	id;

	private String country;
	@Column(name = "tax_rate")
	private int taxRate;
}
