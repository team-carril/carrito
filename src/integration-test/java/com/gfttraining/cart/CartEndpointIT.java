package com.gfttraining.cart;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.gfttraining.cart.api.controller.CartController;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureMockMvc
public class CartEndpointIT {
	@Autowired
	CartController controller;
	@Autowired
	WebApplicationContext webContext;

	@Before
	public void init() {
		RestAssuredMockMvc.webAppContextSetup(webContext);
	}

	@Test
	public void GET_carts_OK() throws Exception {
		RestAssuredMockMvc.get("/carts").then()
				.statusCode(200)
				.assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/carts_array.json"));
	}

	@Test
	public void GET_carts_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		// mvc.perform(MockMvcRequestBuilders.get("/carts?status=BADPARAM"))
		// .andExpect(status().isBadRequest())
		// .andExpect(jsonPath("@.timestamp").isString())
		// .andExpect(jsonPath("@.msg").isString());
	}

}
