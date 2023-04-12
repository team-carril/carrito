package com.gfttraining.cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.service.CartService;

@WebMvcTest(CartController.class)
public class CartControllerIT extends BaseTestWithConstructors {
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@MockBean
	private CartRepository cartRepository;

	@Test
	public void GET_carts_OK() throws Exception {
		List<Cart> l1 = toList(
				cartDto(null, 0, null, null, "DRAFT", null, 0),
				cartDto(null, 0, null, null, "SUBMITTED", null, 0));
		when(cartService.findByStatus("SUBMITTED")).thenReturn(l1);
		mockMvc.perform(get("/carts?status=SUBMITTED"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@[0].id").isString())
				.andExpect(jsonPath("@[0].userId").isNumber())
				.andExpect(jsonPath("@[0].createdAt").isString())
				.andExpect(jsonPath("@[0].updatedAt").isString())
				.andExpect(jsonPath("@[0].totalPrice").isNumber())
				.andExpect(jsonPath("@[0].status").isString())
				.andExpect(jsonPath("@[0].products").isArray());
	}

	@Test
	public void GET_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		mockMvc.perform(get("/carts?status=BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void POST_carts_OK() throws Exception {
		when(cartService.postNewCart(any(User.class))).thenReturn(
			cartDto(null, 1, null, null, "DRAFT", null, 0)
		);
		User user = userDTO(1);
		String json = mapper.writeValueAsString(user);
		
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("@.id").isString())
				.andExpect(jsonPath("@.userId").isNumber())
				.andExpect(jsonPath("@.createdAt").isString())
				.andExpect(jsonPath("@.updatedAt").isString())
				.andExpect(jsonPath("@.totalPrice").isNumber())
				.andExpect(jsonPath("@.status").isString())
				.andExpect(jsonPath("@.products").isArray());
	}

	@Test
	public void POST_carts_BAD_REQUEST_BODY() throws Exception {
		String json = mapper.writeValueAsString(new User());
		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.errorCount").isNumber())
				.andExpect(jsonPath("@.errors").isMap());
	}

	@Test
	public void PATCH_carts_OK() throws Exception {
		UUID id = UUID.randomUUID();
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		when(cartService.addProductToCart(any(ProductFromCatalog.class), any(UUID.class)))
				.thenReturn(cartDto(id, 0, null, null, json, Collections.emptyList(), 0));
		mockMvc.perform(patch("/carts/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void PATCH_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		String json = mapper.writeValueAsString(new ProductFromCatalog());
		mockMvc.perform(patch("/carts/asdf").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void PATCH_carts_BAD_REQUEST_BODY() throws Exception {
		UUID id = UUID.randomUUID();
		String json = mapper.writeValueAsString(new ProductFromCatalog());
		mockMvc.perform(patch("/carts/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.errorCount").isNumber())
				.andExpect(jsonPath("@.errors").isMap());

	}

	@Test
	public void PATCH_carts_NOT_FOUND() throws Exception {
		UUID id = UUID.randomUUID();
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		when(cartService.addProductToCart(any(ProductFromCatalog.class), any(UUID.class)))
				.thenThrow(EntityNotFoundException.class);
		mockMvc.perform(patch("/carts/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	public void DELETE_carts_OK() throws Exception {
		UUID id = UUID.randomUUID();
		mockMvc.perform(delete("/carts/" + id)).andExpect(status().isOk());
		verify(cartService).deleteById(id);
	}

	@Test
	public void DELETE_cart_NOT_FOUND() throws Exception {
		UUID id = UUID.randomUUID();
		when(cartService.deleteById(id)).thenThrow(EntityNotFoundException.class);
		mockMvc.perform(delete("/carts/" + id)).andExpect(status().isNotFound());
		verify(cartService).deleteById(id);
	}
	
	@Test
	public void GET_carts_by_UserId_OK() throws Exception {
		List<Cart> l1 = toList(
				cartDto(null, 0, null, null, "SUBMITTED", null, 0),
				cartDto(null, 0, null, null, "SUBMITTED", null, 0));
		
		when(cartService.findByStatus("SUBMITTED")).thenReturn(l1);
		
		mockMvc.perform(get("/carts?status=SUBMITTED"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("@[0].id").isString())
		.andExpect(jsonPath("@[0].userId").isNumber())
		.andExpect(jsonPath("@[0].createdAt").isString())
		.andExpect(jsonPath("@[0].updatedAt").isString())
		.andExpect(jsonPath("@[0].totalPrice").isNumber())
		.andExpect(jsonPath("@[0].status").isString())
		.andExpect(jsonPath("@[0].products").isArray());		
	}
	
	@Test
	public void GET_carts_by_UserId_NOT_FOUND() throws Exception {		
		String json = mapper.writeValueAsString(1);
		when(cartService.getAllCartEntitiesByUserIdFilteredByStatus(1))
				.thenThrow(EntityNotFoundException.class);
		mockMvc.perform(get("/carts/user/" + 1).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}
}
