package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.Cart;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "CART")
public class CartEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "to_user")
	private int userId;
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "updated_at")
	private Date updatedAt;

	private String status;

	@OneToMany
	@JoinColumn(name = "to_cart", referencedColumnName = "id")
	private List<ProductEntity> products;

	@ManyToOne
	@JoinColumn(name = "to_tax_country", referencedColumnName = "id")
	private TaxCountryEntity taxCountry;

	@Builder
	static public CartEntity create(UUID id, int userId, Date createdAt, Date updatedAt, String status,
			List<ProductEntity> products, TaxCountryEntity taxCountry) {
		CartEntity entity = new CartEntity();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setCreatedAt(createdAt);
		entity.setUpdatedAt(updatedAt);
		entity.setStatus(status);
		entity.setProducts(products);
		entity.setTaxCountry(taxCountry);
		return entity;
	}

	static public Cart toDTO(CartEntity entity) {
		BigDecimal totalPrice = entity.getProducts().stream().reduce(BigDecimal.valueOf(0.0),
				(x, p) -> x.add(p.getTotalPrize()), BigDecimal::add);
		BigDecimal taxRate = BigDecimal.valueOf(entity.getTaxCountry().getTaxRate() / 100); // TODO division de enteros siempre es 0
		totalPrice = totalPrice.add(totalPrice.multiply(taxRate));

		return Cart.builder().id(entity.getId()).userId(entity.getUserId()).createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt()).status(entity.getStatus())
				.products(entity.getProducts().stream().map(ProductEntity::toDTO).collect(Collectors.toList()))
				.totalPrice(totalPrice).taxCountry(TaxCountryEntity.toDTO(entity.getTaxCountry())).build();
	}
}
