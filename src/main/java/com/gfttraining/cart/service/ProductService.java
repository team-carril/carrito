package com.gfttraining.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.jpa.ProductRepository;
import com.gfttraining.cart.jpa.model.ProductEntity;

@Service
public class ProductService {

	ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void updateAllById(int catalogId) {
		throw new UnsupportedOperationException();
	}

	public void deleteAllById(int catalogId) {
		List<ProductEntity> entities = productRepository.findByCatalogId(catalogId);
		throw new UnsupportedOperationException();
	}
}
