package com.gfttraining.cart.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.controller.dto.Cart;

import com.gfttraining.cart.api.controller.dto.ProductFromCatalog;
import com.gfttraining.cart.api.controller.dto.User;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.service.CartService;

@WebMvcTest(CartController.class)
public class CartEndpointTest extends BaseTestWithConstructors {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@MockBean
	private CartRepository cartRepository;

	@Test
	public void returns_200_OK() throws Exception {
		mockMvc.perform(get("/carts")).andExpect(status().isOk());
	}

	@Test
	public void GET_carts_returns_validjson() throws Exception {
		List<Cart> l1 = toList(
				cartDto(null, 0, null, null, "DRAFT", null, 0),
				cartDto(null, 0, null, null, "SUBMITTED", null, 0));
		when(cartService.findByStatus("SUBMITTED")).thenReturn(l1);
		mockMvc.perform(get("/carts?status=SUBMITTED"))
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
		mockMvc.perform(get("/carts?status=BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void POST_carts_returns_OK() throws Exception {
		when(cartService.postNewCart(any(User.class))).thenReturn(
			cartDto(null, 1, null, null, "DRAFT", null, 0)
		);
		String json = mapper.writeValueAsString(new User(1));
		
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@.id").isString())
				.andExpect(jsonPath("@.userId").isNumber())
				.andExpect(jsonPath("@.createdAt").isString())
				.andExpect(jsonPath("@.updatedAt").isString())
				.andExpect(jsonPath("@.totalPrice").isNumber())
				.andExpect(jsonPath("@.status").isString())
				.andExpect(jsonPath("@.products").isArray());
	}

	@Test
	public void POST_carts_bad_requestbody() throws Exception {
		String json = mapper.writeValueAsString(new User()); // will fail with id == 0
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))

				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void POST_carts_returns_OK() throws Exception {
		when(cartService.postNewCart(any(User.class))).thenReturn(
			cartDto(null, 1, null, null, "DRAFT", null, 0)
		);
		String json = mapper.writeValueAsString(new User(1));
		
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@.id").isString())
				.andExpect(jsonPath("@.userId").isNumber())
				.andExpect(jsonPath("@.createdAt").isString())
				.andExpect(jsonPath("@.updatedAt").isString())
				.andExpect(jsonPath("@.totalPrice").isNumber())
				.andExpect(jsonPath("@.status").isString())
				.andExpect(jsonPath("@.products").isArray());
	}

	@Test
	public void POST_carts_bad_requestbody() throws Exception {
		String json = mapper.writeValueAsString(new User()); // will fail with id == 0
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void PATCH_carts_OK() throws Exception {
		UUID id = UUID.randomUUID();
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(1);
		product.setName("test");
		product.setPrice(new BigDecimal(15));
		String json = mapper.writeValueAsString(product);
		mockMvc.perform(patch("/carts/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void PATCH_carts_bad_requestbody() throws Exception {
		String json = mapper.writeValueAsString(new ProductFromCatalog());
		mockMvc.perform(patch("/carts/1").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());

	}

}
