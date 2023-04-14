package com.gfttraining.cart.service;

import java.math.BigDecimal;
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

	private ModelMapper mapper;

	public Mapper() {
		this.mapper = new ModelMapper();
	}

	public Product toProductDTO(ProductEntity entity) {
		return mapper.map(entity, Product.class);
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
		return mapper.map(product, ProductEntity.class);
	}

	public Cart toCartDTO(CartEntity entity) {
		Cart dto = mapper.map(entity, Cart.class);
		BigDecimal totalPrice = entity.getProducts().stream().reduce(BigDecimal.valueOf(0.0),
				(x, p) -> x.add(p.getTotalPrize()), BigDecimal::add);
		totalPrice = totalPrice.stripTrailingZeros();
		dto.setTotalPrice(totalPrice);
		dto.setProducts(entity.getProducts().stream().map((e) -> toProductDTO(e)).collect(Collectors.toList()));
		return dto;
	}

}
