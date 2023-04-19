package com.gfttraining.cart;

import static com.gfttraining.cart.ITConfig.BASE_ERROR_SCHEMA;
import static com.gfttraining.cart.ITConfig.CART_ARRAY_SCHEMA;
import static com.gfttraining.cart.ITConfig.CART_DELETE_ID;
import static com.gfttraining.cart.ITConfig.CART_NOTFOUND_ID;
import static com.gfttraining.cart.ITConfig.CART_SCHEMA;
import static com.gfttraining.cart.ITConfig.CARTa_ID;
import static com.gfttraining.cart.ITConfig.VALIDATION_ERROR_SCHEMA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;

@SpringBootTest
@AutoConfigureMockMvc
public class CartEndpointIT extends BaseTestWithConstructors {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@DisplayName("given no param, when GET /carts, should return only DRAFT status carts")
	@Test
	public void GET_carts_default_param_OK() throws Exception {
		mvc.perform(get("/carts"))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_ARRAY_SCHEMA)));
	}

	@DisplayName("given a status params, when GET /carts, returns results filtered by status")
	@ParameterizedTest
	@MethodSource("provideStatusArguments")
	public void GET_carts_valid_params_OK(String status) throws Exception {

		MvcResult res = mvc.perform(get("/carts?status=" + status))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_ARRAY_SCHEMA)))
				.andReturn();
		Cart carts[] = mapper.readValue(res.getResponse().getContentAsString(), Cart[].class);
		for (Cart c : carts) {
			assertEquals(status, c.getStatus());
		}
	}

	@DisplayName("given invalid param, when GET /carts, should return 400 Error JSON")
	@Test
	public void GET_carts_BAD_QUERY_PARAM() throws Exception {
		mvc.perform(get("/carts?status=BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	@DisplayName("given valid User JSON, when POST request, should return 201 Cart JSON")
	@Test
	public void POST_carts_OK() throws Exception {
		User user = userDTO(1);
		String json = mapper.writeValueAsString(user);

		mvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_SCHEMA)))
				.andExpect(jsonPath("@.userId", is(1)));
	}

	@DisplayName("given User JSON missing properties, when POST, should return 400 Validation Error JSON")
	@Test
	public void POST_carts_BAD_REQUEST_BODY() throws Exception {
		String json = mapper.writeValueAsString(new User());
		mvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR_SCHEMA)));
	}

	@DisplayName("given valid Product JSON, when PATCH, should return 200 Cart JSON")
	@Test
	public void PATCH_carts_OK() throws Exception {
		ProductFromCatalog product = productFromCatalog(1024, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/carts/" + CARTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_SCHEMA)))
				.andExpect(jsonPath("@.products[3].catalogId", is(1024)));
	}

	@DisplayName("given wrong path variable, when PATCH, should return 400 Error JSON")
	@Test
	public void PATCH_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/carts/CRASHANDBURN").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	@DisplayName("given wrong JSON, when PATCH, shoudl return 400 Validation Error JSON")
	@Test
	public void PATCH_carts_BAD_REQUEST_BODY() throws Exception {
		String json = mapper.writeValueAsString(new User());
		mvc.perform(patch("/carts/" + CARTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR_SCHEMA)));

	}

	@DisplayName("given non existing cartId, when PATCH, should return 404 Error JSON")
	@Test
	public void PATCH_carts_NOT_FOUND() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15, 1);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/carts/" + CART_NOTFOUND_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)))
				.andExpect(content().string(containsString(CART_NOTFOUND_ID.toString())));
	}

	@DisplayName("given existing cartId, when DELETE, should return 200")
	@Test
	public void DELETE_carts_OK() throws Exception {
		mvc.perform(delete("/carts/" + CART_DELETE_ID))
				.andExpect(status().isOk());
	}

	@DisplayName("given non existing cartid, when DELETE, should return 404 Error Json")
	@Test
	public void DELETE_cart_NOT_FOUND() throws Exception {
		mvc.perform(delete("/carts/" + CART_NOTFOUND_ID))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString(CART_NOTFOUND_ID.toString())));
	}

	@Test
	public void GET_users_OK() throws Exception {
		MvcResult res = mvc.perform(get("/carts/users/" + 5))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_ARRAY_SCHEMA)))
				.andReturn();

		Cart carts[] = mapper.readValue(res.getResponse().getContentAsString(), Cart[].class);
		for (Cart c : carts) {
			assertEquals("SUBMITTED", c.getStatus());
			assertEquals(5, c.getUserId());
		}
	}

	@Test
	public void GET_users_BAD_PARAM() throws Exception {
		mvc.perform(get("/carts/users/BADPARAM"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	static Stream<Arguments> provideStatusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"));
	}

}
