package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.api.controller.dto.Product;
import com.gfttraining.cart.api.controller.dto.User;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

public class CartServiceTest extends BaseTestWithConstructors {

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

	@Test
	public void handles_empty() {
		when(cartRepository.findAll()).thenReturn(Collections.emptyList());
		cartService.findAll();
		verify(cartRepository).findAll();
	}

	@Test
	public void returns_sorted_dtos() {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		UUID uuidA = UUID.randomUUID();
		UUID uuidB = UUID.randomUUID();
		UUID uuidC = UUID.randomUUID();

		List<ProductEntity> p1 = toList(productEntity(1, 1, "test_item", "asdf", uuidA, 5.0, 1),
				productEntity(2, 2, "test_item", "asdf", uuidA, 5.0, 1),
				productEntity(3, 3, "test_item", "asdf", uuidA, 5.0, 1));
		List<Product> p2 = toList(productDto(1, 1, "test_item", "asdf", uuidA, 5.0, 1),
				productDto(2, 2, "test_item", "asdf", uuidA, 5.0, 1),
				productDto(3, 3, "test_item", "asdf", uuidA, 5.0, 1));

		List<CartEntity> listInput = toList(
				cartEntity(uuidA, 1, testDate, testDate, "DRAFT", p1),
				cartEntity(uuidB, 1, testDate, testDate, "SUBMITTED", p1),
				cartEntity(uuidC, 1, testDate, testDate, "DRAFT", p1));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", p2, 15),
				cartDto(uuidC, 1, testDate, testDate, "DRAFT", p2, 15),
				cartDto(uuidB, 1, testDate, testDate, "SUBMITTED", p2, 15));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);
	}

	@ParameterizedTest
	@MethodSource("statusArguments")
	public void returns_filtered_by_status(String status) {
		cartService.findByStatus(status);
		verify(cartRepository).findByStatus(status);
	}

	@Test
	public void handles_empty_product_list() {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		UUID uuidA = UUID.randomUUID();

		List<CartEntity> listInput = toList(cartEntity(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList()));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(), 0));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);
	}

	@Test
	public void create_calls_repository() {
		User user = new User();
		user.setId(0);
		when(cartRepository.save(any(CartEntity.class)))
				.thenReturn(cartEntity(null, 0, null, null, null, Collections.emptyList()));
		cartService.postNewCart(user);
		verify(cartRepository).save(any(CartEntity.class));
	}

	@Test
	public void add_product_existing_product() {
		UUID uuid = UUID.randomUUID();
		Product product = productDto(1, 1, null, null, uuid, 0, 1);
		CartEntity entity = cartEntity(uuid, 0, null, null, null, toList(productEntity(1, 1, null, null, uuid, 0, 1)));

		when(cartRepository.findById(uuid)).thenReturn(Optional.of(entity));
		when(cartRepository.saveAndFlush(entity)).thenReturn(entity);
		cartService.addProductToCart(product, uuid);
		verify(cartRepository).findById(uuid);
		verify(cartRepository).saveAndFlush(entity);
	}

	@Test
	@Disabled // TODO
	public void add_product_new_product() {
	}

	@Test
	public void add_product_throws_cart_not_found() {
		UUID uuid = UUID.randomUUID();
		when(cartRepository.findById(uuid)).thenReturn(Optional.ofNullable(null));
		assertThrows(EntityNotFoundException.class, () -> cartService.addProductToCart(new Product(), uuid));
	}

	@Test
	public void delete_product_OK() {
		UUID id = UUID.randomUUID();
		when(cartRepository.findById(id)).thenReturn(Optional.of(new CartEntity()));
		cartService.deleteById(id);
		verify(cartRepository).delete(any(CartEntity.class));
	}

	@Test
	public void delete_product_throws_NOT_FOUND() {
		UUID id = UUID.randomUUID();
		when(cartRepository.findById(id)).thenReturn(Optional.ofNullable(null));
		assertThrows(EntityNotFoundException.class, () -> cartService.deleteById(id));
	}

	static Stream<Arguments> statusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"));
	}
}
