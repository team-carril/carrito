package com.gfttraining.cart.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.TaxCountry;

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

	static public TaxCountry toDTO (TaxCountryEntity entity)
	{
		return TaxCountry.builder()
		.country(entity.getCountry())
		.taxRate(entity.getTaxRate())
		.build();
	}
}
