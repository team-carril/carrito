package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gfttraining.cart.api.controller.dto.Cart;

import lombok.Data;

@Entity
@Data
@Table(name = "CART")
public class CartEntity {

	private String id; 
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

	static public Cart toDTO(CartEntity entity)
	{
		BigDecimal totalPrice = entity.getProducts().stream()
		.reduce(BigDecimal.valueOf(0), 
		(x, p) -> x.add(p.getTotalPrize()), BigDecimal::add);
		totalPrice = totalPrice.multiply(BigDecimal.valueOf(entity.getTaxCountry().getTaxRate() / 100));

		return Cart.builder()
		.id(entity.getId())
		.userId(entity.getUserId())
		.createdAt(entity.getCreatedAt())
		.updateAt(entity.getUpdatedAt())
		.status(entity.getStatus())
		.products(entity.getProducts().stream().map(ProductEntity::toDTO).collect(Collectors.toList()))
		.totalPrice(totalPrice) 
		.taxCountry(TaxCountryEntity.toDTO(entity.getTaxCountry())) 
		.build();
	}
}
