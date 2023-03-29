package com.gfttraining.cart.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.service.CartService;

@WebMvcTest(CartController.class)
public class CartEndpointTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@Test
	public void returns_200_OK() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/carts")).andExpect(MockMvcResultMatchers.status().isOk());
	}

}
