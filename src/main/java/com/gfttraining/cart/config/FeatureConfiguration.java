package com.gfttraining.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "featureflag")
@Data
public class FeatureConfiguration {
	private Boolean updateAllByIdEnabled;
}
