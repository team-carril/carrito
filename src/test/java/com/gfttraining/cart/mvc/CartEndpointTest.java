package com.gfttraining.cart.mvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.controller.dto.Cart;
import com.gfttraining.cart.service.CartService;

@WebMvcTest(CartController.class)
public class CartEndpointTest extends BaseTestWithConstructors {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@Test
	public void returns_200_OK() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/carts")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void GET_carts_returns_validjson() throws Exception {
		List<Cart> l1 = toList(
				cartDto(null, 0, null, null, "DRAFT", null, 0),
				cartDto(null, 0, null, null, "SUBMITTED", null, 0));
		when(cartService.findByStatus("SUBMITTED")).thenReturn(l1);
		mockMvc.perform(MockMvcRequestBuilders.get("/carts?status=SUBMITTED"))
				.andExpect(jsonPath("@[0].id").isString())
				.andExpect(jsonPath("@[0].userId").isNumber())
				.andExpect(jsonPath("@[0].createdAt").isString())
				.andExpect(jsonPath("@[0].updatedAt").isString())
				.andExpect(jsonPath("@[0].totalPrice").isNumber())
				.andExpect(jsonPath("@[0].status").isString())
				.andExpect(jsonPath("@[0].products").isArray());
	}

	@Test
	public void GET_carts_badparam_returns_errorjson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/carts?status=BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

}
