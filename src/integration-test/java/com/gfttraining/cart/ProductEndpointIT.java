package com.gfttraining.cart;

//import static com.gfttraining.cart.ITConfig.BASE_ERROR_SCHEMA;
//import static com.gfttraining.cart.ITConfig.CART_COUNT_SCHEMA;
//import static com.gfttraining.cart.ITConfig.PRODUCT_NOTFOUND_ID;
//import static com.gfttraining.cart.ITConfig.PRODUCTa_ID;
//import static com.gfttraining.cart.ITConfig.PRODUCTa_RESULT;
//import static com.gfttraining.cart.ITConfig.PRODUCTb_ID;
//import static com.gfttraining.cart.ITConfig.PRODUCTb_RESULT;
//import static com.gfttraining.cart.ITConfig.VALIDATION_ERROR_SCHEMA;
import static com.gfttraining.cart.ITConfig.BASE_ERROR_SCHEMA;
import static com.gfttraining.cart.ITConfig.CART_COUNT_SCHEMA;
import static com.gfttraining.cart.ITConfig.PRODUCT_NOTFOUND_ID;
import static com.gfttraining.cart.ITConfig.PRODUCTa_ID;
import static com.gfttraining.cart.ITConfig.PRODUCTa_RESULT;
import static com.gfttraining.cart.ITConfig.PRODUCTb_ID;
import static com.gfttraining.cart.ITConfig.PRODUCTb_RESULT;
import static com.gfttraining.cart.ITConfig.VALIDATION_ERROR_SCHEMA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.api.dto.ProductFromCatalog;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductEndpointIT extends BaseTestWithConstructors {

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
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_COUNT_SCHEMA)))
				.andExpect(jsonPath("@.cartsChanged", is(PRODUCTa_RESULT)));
	}

	@DisplayName("given non existing catalogId, when PATCH /products, should return 404 Error JSON")
	@Test
	public void PATCH_products_NOT_FOUND() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/products/" + PRODUCT_NOTFOUND_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)))
				.andExpect(content().string(containsString(PRODUCT_NOTFOUND_ID.toString())));
	}

	@DisplayName("given invalid Product JSON, when PATCH /products, should return 400 Validation Error JSON")
	@Test
	public void PATCH_products_BAD_REQUEST_BODY() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		String json = mapper.writeValueAsString(product);

		mvc.perform(patch("/products/" + PRODUCTa_ID).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(VALIDATION_ERROR_SCHEMA)));
	}

	@DisplayName("given invalid path variable, when PATCH /products, should return 400 Error JSON")
	@Test
	public void PATCH_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/products/asdf").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	@DisplayName("given existing catalogId, when DELETE /products, should return 200 CartCount JSON")
	@Test
	public void DELETE_products_OK() throws Exception {
		mvc.perform(delete("/products/" + PRODUCTb_ID))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_COUNT_SCHEMA)))
				.andExpect(jsonPath("@.cartsChanged", is(PRODUCTb_RESULT)));
	}

	@DisplayName("given non existing catalogId, when DELETE /products, should return 404 Error JSON")
	@Test
	public void DELETE_products_NOT_FOUND() throws Exception {
		mvc.perform(delete("/products/" + PRODUCT_NOTFOUND_ID))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)))
				.andExpect(content().string(containsString(PRODUCT_NOTFOUND_ID.toString())));
	}

	@DisplayName("given non existing catalogId, when DELETE /products, should return 404 Error JSON")
	@Test
	public void DELETE_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		mvc.perform(delete("/products/asdf"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

}
