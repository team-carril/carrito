package com.gfttraining.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "external-services")
@Data
public class ExternalServicesConfiguration {

	public String catalogUrl;
	public String usersUrl;

}
