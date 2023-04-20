package com.gfttraining.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;

import com.gfttraining.cart.config.ExternalServicesConfiguration;
import com.gfttraining.cart.config.RatesConfiguration;

import lombok.Generated;

@SpringBootApplication
@EnableConfigurationProperties({ RatesConfiguration.class, ExternalServicesConfiguration.class })
@EnableRetry
public class CartApplication {

	@Generated
	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}

}
