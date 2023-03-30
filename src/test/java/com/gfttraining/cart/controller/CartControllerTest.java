package com.gfttraining.cart.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.api.controller.CartController;
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

	@Test
	public void findAllCartTest() {
		cartController.findAllCarts();
		verify(cartService).findAll();
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

	static Stream<Arguments> statusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"),
				Arguments.of("ALL"),
				Arguments.of("INVALID PARAM"),
				Arguments.of(""));
	}

}
