package com.gfttraining.cart.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
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

import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.controller.dto.Product;
import com.gfttraining.cart.api.controller.dto.ProductFromCatalog;
import com.gfttraining.cart.api.controller.dto.User;
import com.gfttraining.cart.exception.BadRequestBodyException;
import com.gfttraining.cart.exception.BadRequestParamException;
import com.gfttraining.cart.service.CartService;

class CartControllerTest {

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
	public void create_cart() throws BadRequestBodyException {
		User user = new User(1);
		cartController.createCart(user);
		verify(cartService).postNewCart(user);
	}

	@Test
	public void add_product_to_cart() throws BadRequestBodyException {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(1);
		product.setName("test");
		product.setPrice(new BigDecimal(15));
		cartController.addProductToCart(product, UUID.randomUUID());
		verify(cartService).addProductToCart(any(Product.class), any(UUID.class));
	}

	@Test
	public void delete_cart() throws EntityNotFoundException {
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

}
