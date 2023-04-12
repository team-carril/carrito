package com.gfttraining.cart;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.api.controller.CartController;
import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.jpa.CartRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CartEndpointIT extends BaseTestWithConstructors {
	static final String CART_ARRAY = "schema/carts_array.json";
	static final String CART = "schema/cart.json";
	static final String PRODUCT = "schema/product.json";
	static final String BASE_ERROR = "schema/base_error.json";
	static final String VALIDATION_ERROR = "schema/validation_error.json";
	static final UUID CARTa_ID = UUID.fromString("6e7baf09-9877-4c26-a4fb-21e0e2572819");
	static final UUID CARTb_ID = UUID.fromString("0f6f7a4f-1097-4624-8973-ae6966728647");

	@Autowired
	CartController controller;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	CartRepository cartRepository;

	@DisplayName("given no param, when GET /carts, should return only DRAFT status carts")
	@Test
	public void GET_carts_default_param_OK() throws Exception {
		mvc.perform(get("/carts"))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_ARRAY)));
	}

	@DisplayName("given a status params, when GET /carts, returns results filtered by status")
	@ParameterizedTest
	@MethodSource("provideStatusArguments")
	public void GET_carts_valid_params_OK(String status) throws Exception {

		MvcResult res = mvc.perform(get("/carts?status=" + status))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_ARRAY)))
				.andReturn();
		Cart carts[] = mapper.readValue(res.getResponse().getContentAsString(), Cart[].class);
		for (Cart c : carts) {
			assertEquals(status, c.getStatus());
		}
	}

	@DisplayName("given invalid param, when GET /carts, should return 400 Error JSON")
	@Test
	public void GET_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		mvc.perform(get("/carts?status=BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

	@DisplayName("given valid User JSON, when POST request, should return 201 Cart JSON")
	@Test
	public void POST_carts_OK() throws Exception {
		User user = userDTO(1);
		String json = mapper.writeValueAsString(user);

		mvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART)));
	}

	@DisplayName("given User JSON missing properties, when POST, should return 400 Validation Error JSON")
	@Test
	public void POST_carts_BAD_REQUEST_BODY() throws Exception {
		String json = mapper.writeValueAsString(new User());
		mvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR)));
	}

	@DisplayName("given valid Product JSON, when PATCH, should return 200 Cart JSON")
	@Test
	public void PATCH_carts_OK() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/carts/" + CARTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART)));
		// CartEntity modifiedCart = cartRepository.findById(CARTa_ID).get();
		// assertEquals(product.getId(),
		// modifiedCart.getProducts().get(3).getCatalogId());
	}

	@DisplayName("given wrong path variable, when PATCH, should return 400 Error JSON")
	@Test
	public void PATCH_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/carts/CRASHANDBURN").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

	@DisplayName("given wrong JSON, when PATCH, shoudl return 400 Validation Error JSON")
	@Test
	public void PATCH_carts_BAD_REQUEST_BODY() throws Exception {
		String json = mapper.writeValueAsString(new User());
		mvc.perform(patch("/carts/" + CARTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR)));

	}

	@DisplayName("given non existing cartId, when PATCH, should return 404 Error JSON")
	@Test
	public void PATCH_carts_NOT_FOUND() throws Exception {
		UUID notFoundId = UUID.randomUUID();
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/carts/" + notFoundId).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

	@DisplayName("given existing cartId, when DELETE, should return 200")
	@Test
	public void DELETE_carts_OK() throws Exception {
		mvc.perform(delete("/carts/" + CARTb_ID))
				.andExpect(status().isOk());
		// TODO repo call
	}

	@DisplayName("given non existing cartid, when DELETE, should return 404 Error Json")
	@Test
	public void DELETE_cart_NOT_FOUND() throws Exception {
		UUID notFoundId = UUID.randomUUID();
		mvc.perform(delete("/carts/" + notFoundId))
				.andExpect(status().isNotFound());
		// TODO repo call
	}

	static Stream<Arguments> provideStatusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"));
	}

}
