package com.gfttraining.cart.api.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class TaxCountry {

	private String country;
	private Integer taxRate;

	@Builder
	static public TaxCountry create(String country, int taxRate) {
		TaxCountry taxCountry = new TaxCountry();
		taxCountry.setCountry(country);
		taxCountry.setTaxRate(taxRate);
		return taxCountry;
	}

}
