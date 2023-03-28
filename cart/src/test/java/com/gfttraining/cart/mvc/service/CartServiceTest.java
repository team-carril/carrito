package com.gfttraining.cart.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.Product;
import com.gfttraining.cart.api.controller.dto.TaxCountry;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;
import com.gfttraining.cart.jpa.model.TaxCountryEntity;
import com.gfttraining.cart.service.CartService;

public class CartServiceTest {

	@Mock
	CartRepository cartRepository;

	@InjectMocks
	CartService cartService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void findAll_calls_repository() {
		cartService.findAll();
		verify(cartRepository).findAll();
	}

	@Test public void
	returns_valid_dto()
	{
		Date testDate = new Date(777);
		UUID uuidA = UUID.randomUUID();
		UUID uuidB = UUID.randomUUID();
		UUID uuidC = UUID.randomUUID();

		List<ProductEntity> p1 = toList(
			productEntity(1, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productEntity(2, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productEntity(3, "test_item", "asdf", uuidA, 1, 5.0, 1)
		) ;
		List<Product> p2 = toList(
			productDto(1, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productDto(2, "test_item", "asdf", uuidA, 1, 5.0, 1),
			productDto(3, "test_item", "asdf", uuidA, 1, 5.0, 1)
		);

		List<CartEntity> listInput = toList(
			cartEntity(uuidA, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0)),
			cartEntity(uuidB, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0)),
			cartEntity(uuidC, 1, testDate, testDate, "DRAFT", p1, taxCountryEntity("SPAIN", 0))
		);
		List<Cart> listExpected = toList(
			cartDto(uuidA, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0),
			cartDto(uuidB, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0),
			cartDto(uuidC, 1, testDate, testDate, "DRAFT", p2, taxCountry("SPAIN", 0), 15.0)
		);
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();

		assertEquals(listExpected, listActual);
	}

	@Test
	@Disabled
	public void handles_empty_product_list() {
		Date testDate = new Date(777);
		UUID uuidA = UUID.randomUUID();

		List<CartEntity> listInput = toList(
				cartEntity(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(),
						taxCountryEntity("SPAIN", 21)));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(), taxCountry("SPAIN", 21),
						(float) 0));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);

	}

	private TaxCountryEntity taxCountryEntity(String name, int taxRate) {
		TaxCountryEntity entity = new TaxCountryEntity();
		entity.setCountry(name);
		entity.setTaxRate(taxRate);
		return entity;
	}

	private TaxCountry taxCountry(String name, int taxRate) {
		return TaxCountry.builder().country(name).taxRate(taxRate).build();
	}

	private CartEntity cartEntity(UUID id, int userId, Date createdAt,
			Date updatedAt, String status, List<ProductEntity> products, TaxCountryEntity taxCountry) {
		return CartEntity.builder()
				.id(id).userId(userId)
				.createdAt(createdAt).updatedAt(updatedAt)
				.status(status).products(products).taxCountry(taxCountry)
				.build();

	}

	private Cart cartDto(UUID id, int userId, Date createdAt,
			Date updatedAt, String status, List<Product> products, TaxCountry taxCountry, double totalPrice) {

		return Cart.builder().id(id).userId(userId)
				.createdAt(createdAt).updateAt(updatedAt).status(status)
				.products(products).taxCountry(taxCountry)
				.totalPrice(BigDecimal.valueOf(totalPrice)).build();

	}

	private List<CartEntity> toList(CartEntity... entities) {
		return Arrays.asList(entities);
	}

	private List<Cart> toList(Cart... dtos) {
		return Arrays.asList(dtos);
	}

	private ProductEntity productEntity(int id, String name, String description, UUID cartId, int category,
			double price, int quantity) {
		return ProductEntity.builder().id(id).name(name).cartId(cartId).description(description).category(category)
				.price(BigDecimal.valueOf(price)).quantity(quantity).build();
	}

	private Product productDto(int id, String name, String description, UUID cartId, int category, double price, int quantity) {
		return Product.builder().id(id).name(name).category(category).description(description).price(BigDecimal.valueOf(price))
				.quantity(quantity).cartId(cartId).build();
	}

	private List<ProductEntity> toList(ProductEntity... entities) {
		return Arrays.asList(entities);
	}

	private List<Product> toList(Product... dtos) {
		return Arrays.asList(dtos);
	}

}
