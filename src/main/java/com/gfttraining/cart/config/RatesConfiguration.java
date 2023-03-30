package com.gfttraining.cart.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "rates")
@Data
public class RatesConfiguration {

	private Map<String, Integer> country;
	private Map<String, Integer> paymentMethod;

}
