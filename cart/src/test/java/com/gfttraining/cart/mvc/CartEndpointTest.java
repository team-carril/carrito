package com.gfttraining.cart.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gfttraining.cart.api.controller.CartController;

@WebMvcTest(CartController.class)
public class CartEndpointTest {

	@Autowired
	private MockMvc mockMvc;

	@Test public void
	returns_200_OK() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/carts"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
