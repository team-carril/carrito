package com.gfttraining.cart.service;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

@Component
public class Mapper {

	private ModelMapper mapperModel;

	public Mapper() {
		this.mapperModel = new ModelMapper();
	}

	public Product toProductDTO(ProductEntity entity) {
		return mapperModel.map(entity, Product.class);
	}

	public Product toProductDTO(ProductFromCatalog productFromCatalog) {
		return Product.builder().catalogId(productFromCatalog.getId())
				.name(productFromCatalog.getName())
				.description(productFromCatalog.getDescription())
				.price(productFromCatalog.getPrice())
				.quantity(productFromCatalog.getQuantity())
				.build();
	}

	public ProductEntity toProductEntity(Product product) {
		return mapperModel.map(product, ProductEntity.class);
	}

	public Cart toCartDTO(CartEntity entity) {
		Cart dto = mapperModel.map(entity, Cart.class);
		dto.setProducts(entity.getProducts().stream().map(this::toProductDTO).collect(Collectors.toList()));
		if (entity.getStatus().equals("DRAFT"))
			dto.setTotalPrice(entity.calculatePrice());
		return dto;
	}

}
