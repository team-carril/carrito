package com.gfttraining.cart.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.ProductRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

@Service
public class ProductService {

	ProductRepository productRepository;
	CartRepository cartRepository;
	Mapper mapper;

	public ProductService(ProductRepository productRepository, CartRepository cartRepository, Mapper mapper) {
		this.productRepository = productRepository;
		this.cartRepository = cartRepository;
		this.mapper = mapper;
	}

	public List<ProductEntity> findByCatalogId(int catalogId) {
		List<ProductEntity> entities = productRepository.findByCatalogId(catalogId);
		if (entities.isEmpty())
			throw new EntityNotFoundException("No product with id: " + catalogId + " found.");
		entities = entities.stream()
				.filter((e) -> checkCartStatus(e.getCartId()).equals("DRAFT"))
				.collect(Collectors.toList());
		if (entities.isEmpty())
			throw new EntityNotFoundException("No product with id: " + catalogId + " found.");
		return entities;
	}

	public CartCountDTO updateAllById(ProductFromCatalog productFromCatalog, int catalogId) {
		Product product = mapper.toProductDTO(productFromCatalog);
		List<ProductEntity> entities = findByCatalogId(catalogId);
		for (ProductEntity entity : entities)
			updateEntity(product, entity);
		CartCountDTO counter = new CartCountDTO();
		counter.setCartsChanged(entities.size());
		productRepository.saveAllAndFlush(entities);
		return counter;
	}

	public CartCountDTO deleteAllById(int catalogId) {
		List<ProductEntity> entities = findByCatalogId(catalogId);
		CartCountDTO counter = new CartCountDTO();
		counter.setCartsChanged(entities.size());
		productRepository.deleteAll(entities);
		return counter;
	}

	private void updateEntity(Product product, ProductEntity entity) {
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPrice(product.getPrice());
	}

	private String checkCartStatus(UUID cartId) {
		Optional<CartEntity> entity = cartRepository.findById(cartId);
		if (entity.isEmpty())
			throw new EntityNotFoundException("No cart associated with this product");
		return entity.get().getStatus();
	}
}
