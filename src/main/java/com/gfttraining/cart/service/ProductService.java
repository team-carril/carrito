package com.gfttraining.cart.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.jpa.ProductRepository;
import com.gfttraining.cart.jpa.model.ProductEntity;

@Service
public class ProductService {

	ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductEntity> findByCatalogId(int catalogId) {
		List<ProductEntity> entities = productRepository.findByCatalogId(catalogId);
		if (entities.isEmpty())
			throw new EntityNotFoundException("No product with id: " + catalogId + " found.");
		return entities;
	}

	public void updateAllById(Product product, int catalogId) {
		List<ProductEntity> entities = findByCatalogId(catalogId);
		for (ProductEntity entity : entities)
			updateEntity(product, entity);
		productRepository.saveAllAndFlush(entities);
	}

	public void deleteAllById(int catalogId) {
		List<ProductEntity> entities = findByCatalogId(catalogId);
		productRepository.deleteAll(entities);
	}

	private void updateEntity(Product product, ProductEntity entity) {
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPrice(product.getPrice());
	}
}
