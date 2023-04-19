package com.gfttraining.cart;

import static com.gfttraining.cart.ITConfig.BASE_ERROR_SCHEMA;
import static com.gfttraining.cart.ITConfig.CART_SCHEMA;
import static com.gfttraining.cart.ITConfig.CART_SUBMITTED_ID;
import static com.gfttraining.cart.ITConfig.CARTa_ID;
import static com.gfttraining.cart.ITConfig.CARTb_ID;
import static com.gfttraining.cart.ITConfig.CARTc_ID;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.api.dto.User;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

@SpringBootTest
@AutoConfigureMockMvc
public class ExternalMicroServicesIT extends BaseTestWithConstructors {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	static final String USERS_ROUTE = "/users/bInfo/";
	static final String CATALOG_ROUTE = "/products/";
	static final String UPDATE_CATALOG_ROUTE = CATALOG_ROUTE + "updateStock/";
	static final String INFO_CATALOG_ROUTE = CATALOG_ROUTE + "id/";

	@RegisterExtension
	static WireMockExtension catalogMock = WireMockExtension.newInstance().options(wireMockConfig().port(8081)).build();
	@RegisterExtension
	static WireMockExtension usersMock = WireMockExtension.newInstance().options(wireMockConfig().port(8082)).build();

	@DisplayName("given Cart with a status of submitted, when POST carts/submit, should 404 ERROR JSON")
	@Test
	public void cart_is_submitted() throws Exception {

		mvc.perform(post("/carts/submit/" + CART_SUBMITTED_ID))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));

	}

	@DisplayName("given any service down, when POST carts/submit, should 503 Error JSON")
	@Test
	public void service_down() throws IOException, Exception {

		usersMock.stubFor(
				WireMock.get(USERS_ROUTE + 1).willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

		mvc.perform(post("/carts/submit/" + CARTa_ID))
				.andExpect(status().isServiceUnavailable())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	@DisplayName("given any service 404s, when POST carts/submit, should 404 Error JSON")
	@Test
	public void service_404() throws Exception {
		User u1 = userDTO(1, "VISA", "SPAIN");
		String json = mapper.writeValueAsString(u1);
		usersMock.stubFor(WireMock.get(USERS_ROUTE +
				u1.getId()).willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(json).withStatus(200)));

		// if not mocked, Catalog is returning 404
		mvc.perform(post("/carts/submit/" + CARTa_ID))
				.andExpect(status().isNotFound())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)));
	}

	@DisplayName("given unrecognized user information, when POST carts/submit, should 409 Error JSON")
	@Test
	public void invalid_user_info() throws Exception {
		User u1 = userDTO(4, "RUBICARD", "NARNIA");
		String json = mapper.writeValueAsString(u1);
		usersMock.stubFor(WireMock.get(USERS_ROUTE +
				u1.getId()).willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(json).withStatus(200)));

		mvc.perform(post("/carts/submit/" + CARTc_ID))
				.andExpect(status().isConflict())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)))
				.andExpect(content().string(containsString("Unrecognized")));
	}

	@DisplayName("given insufficient stock, when POST carts/submit, should 409 Error JSON")
	@Test
	public void out_of_stock() throws Exception {
		User u1 = userDTO(1, "VISA", "SPAIN");
		String json = mapper.writeValueAsString(u1);
		usersMock.stubFor(WireMock.get(USERS_ROUTE +
				u1.getId()).willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(json).withStatus(200)));
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 2).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(2, 10, 1))))); // out of stock
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 5).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(5, 10, 5)))));
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 4).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(4, 10, 5)))));

		mvc.perform(post("/carts/submit/" + CARTa_ID))
				.andExpect(status().isConflict())
				.andExpect(content().string(matchesJsonSchemaInClasspath(BASE_ERROR_SCHEMA)))
				.andExpect(content().string(containsString("out of stock")));
	}

	@DisplayName("given valid data in external services, when POST carts/submit, should 200 CART JSON")
	@Test
	public void happy_path() throws Exception {
		User u6 = userDTO(6, "TRANSFER", "ESTONIA");
		String json = mapper.writeValueAsString(u6);
		usersMock.stubFor(WireMock.get(USERS_ROUTE +
				u6.getId()).willReturn(WireMock.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(json).withStatus(200)));
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 7).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(7, 10, 100)))));
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 41).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(41, 10, 100)))));
		catalogMock.stubFor(WireMock.get(INFO_CATALOG_ROUTE + 62).willReturn(WireMock.aResponse()
				.withHeader("Content-Type", "application/json")
				.withBody(mapper.writeValueAsString(productFromCatalog(62, 10, 100)))));

		catalogMock.stubFor(WireMock.put(UPDATE_CATALOG_ROUTE + 7).willReturn(WireMock.aResponse().withStatus(200)));
		catalogMock.stubFor(WireMock.put(UPDATE_CATALOG_ROUTE + 41).willReturn(WireMock.aResponse().withStatus(200)));
		catalogMock.stubFor(WireMock.put(UPDATE_CATALOG_ROUTE + 62).willReturn(WireMock.aResponse().withStatus(200)));

		mvc.perform(post("/carts/submit/" + CARTb_ID))
				.andExpect(status().isOk())
				.andExpect(content().string(matchesJsonSchemaInClasspath(CART_SCHEMA)))
				.andExpect(jsonPath("@.id", is(CARTb_ID.toString())))
				.andExpect(jsonPath("@.status", is("SUBMITTED")))
				.andExpect(jsonPath("@.totalPrice", is(1603)));
	}

}
