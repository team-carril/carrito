package com.gfttraining.cart.jpa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private String status;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "to_cart", referencedColumnName = "id")
	private List<ProductEntity> products;

	@JoinColumn(name = "total_price")
	private BigDecimal totalPrice;

	@Builder
	static public CartEntity create(UUID id, int userId, LocalDateTime createdAt, LocalDateTime updatedAt,
			String status,
			List<ProductEntity> products, BigDecimal totalPrice) {
		CartEntity entity = new CartEntity();
		entity.setId(id);
		entity.setUserId(userId);
		entity.setCreatedAt(createdAt);
		entity.setUpdatedAt(updatedAt);
		entity.setStatus(status);
		entity.setProducts(products);
		entity.setTotalPrice(totalPrice);
		return entity;
	}

	public BigDecimal calculatePrice() {
		BigDecimal totalPrice = products.stream().reduce(BigDecimal.valueOf(0.0),
				(x, p) -> x.add(p.getTotalPrize()), BigDecimal::add);
		totalPrice = totalPrice.stripTrailingZeros();
		return totalPrice;

	}
}
