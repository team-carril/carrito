package com.gfttraining.cart;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.ProductFromCatalog;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductEndpointIT extends BaseTestWithConstructors {

	static final String BASE_ERROR = "schema/base_error.json";
	static final String CART_COUNT = "schema/cart_count.json";
	static final String VALIDATION_ERROR = "schema/validation_error.json";
	static final int PRODUCTa_ID = 2;
	static final int PRODUCTb_ID = 5;
	static final int NOTFOUND_ID = 777;

	@Autowired
	ProductController controller;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@DisplayName("given valid Product JSON, when PATCH /products, should return 200 CartCount JSON")
	@Test
	public void PATCH_products_OK() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/products/" + PRODUCTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_COUNT)));
	}

	@DisplayName("given non existing catalogId, when PATCH /products, should return 404 Error JSON")
	@Test
	public void PATCH_products_NOT_FOUND() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/products/777").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

	@DisplayName("given invalid Product JSON, when PATCH /products, should return 400 Validation Error JSON")
	@Test
	public void PATCH_products_BAD_REQUEST_BODY() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/products/2").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR)));
	}

	@DisplayName("given invalid path variable, when PATCH /products, should return 400 Error JSON")
	@Test
	public void PATCH_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/products/asdf").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
		// TODO VALIDATE ERRROR STRING
		// TODO Target nums for counts
	}

	@DisplayName("given existing catalogId, when DELETE /products, should return 200 CartCount JSON")
	@Test
	public void DELETE_products_OK() throws Exception {
		mvc.perform(delete("/products/5"))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_COUNT)));
	}

	@DisplayName("given non existing catalogId, when DELETE /products, should return 404 Error JSON")
	@Test
	public void DELETE_products_NOT_FOUND() throws Exception {
		mvc.perform(delete("/products/777"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

	@DisplayName("given non existing catalogId, when DELETE /products, should return 404 Error JSON")
	@Test
	public void DELETE_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		mvc.perform(delete("/products/asdf"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR)));
	}

}
