package com.gfttraining.cart.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.exception.BadRequestParamException;
import com.gfttraining.cart.exception.InvalidUserDataException;
import com.gfttraining.cart.exception.OutOfStockException;
import com.gfttraining.cart.exception.RemoteServiceException;
import com.gfttraining.cart.service.CartService;

class CartControllerTest extends BaseTestWithConstructors {

	@Mock
	CartService cartService;

	@InjectMocks
	CartController cartController;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@ParameterizedTest
	@MethodSource("statusArguments")
	public void findByStatus(String status) throws BadRequestParamException {
		if (!CartController.isValidStatus(status)) {
			assertThrows(BadRequestParamException.class, () -> cartController.findByStatus(status));
			return;
		}
		cartController.findByStatus(status);
		if (status == null) {
			verify(cartService).findByStatus("DRAFT");
		} else if (status.equals("ALL")) {
			verify(cartService).findAll();
		} else {
			verify(cartService).findByStatus(status);
		}
	}

	@Test
	public void createCart() throws BadRequestBodyException {
		User user = new User();
		Cart cart = cartDto(UUID.randomUUID(), 1, null, null, null, null, 0);
		when(cartService.postNewCart(user)).thenReturn(cart);
		cartController.createCart(user);
		verify(cartService).postNewCart(user);
	}

	@Test
	public void addProductToCart() throws BadRequestBodyException {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);

		Cart cart = cartDto(null, 1, null, null, null, Collections.emptyList(), 0);
		when(cartService.addProductToCart(any(ProductFromCatalog.class), any(UUID.class))).thenReturn(cart);
		cartController.addProductToCart(product, UUID.randomUUID());
		verify(cartService).addProductToCart(any(ProductFromCatalog.class), any(UUID.class));
	}

	@Test
	public void deleteCart() throws EntityNotFoundException {
		UUID id = UUID.randomUUID();
		cartController.deleteCartById(id);
		verify(cartService).deleteById(id);
	}

	static Stream<Arguments> statusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"),
				Arguments.of("ALL"),
				Arguments.of("INVALID PARAM"),
				Arguments.of(""));
	}

	@Test
	public void getCartsByUserIdTest() throws EntityNotFoundException {
		cartController.getAllCartEntitiesByUserIdFilteredByStatus(1);
		verify(cartService).getAllCartEntitiesByUserIdFilteredByStatus(1);
	}

	@Test
	public void validateCart() throws RemoteServiceException, OutOfStockException, InvalidUserDataException { // TODO
		UUID id = UUID.randomUUID();
		cartController.validateCart(id);
		verify(cartService).validateCart(id);
	}

}
