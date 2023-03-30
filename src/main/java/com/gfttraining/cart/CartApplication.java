package com.gfttraining.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.gfttraining.cart.config.RatesConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(RatesConfiguration.class)
public class CartApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}

}
