package com.gfttraining.cart.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;

@Service
public class RestService {

	RestTemplate restTemplate;

	public RestService() {
		this.restTemplate = new RestTemplate();
	}

	public User fetchUserInfo(int userId) throws EntityNotFoundException {
		throw new UnsupportedOperationException();
	}

	public ProductFromCatalog fetchProductFromCatalog(int catalogId) throws EntityNotFoundException {
		throw new UnsupportedOperationException();
	}

}
