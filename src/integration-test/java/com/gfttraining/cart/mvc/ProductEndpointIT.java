package com.gfttraining.cart.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

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
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.service.ProductService;
import com.gfttraining.cart.api.dto.Product;

@WebMvcTest(ProductController.class)
public class ProductEndpointIT extends BaseTestWithConstructors {

	@Autowired
	private MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ProductService productService;

	@Test
	public void PATCH_products_OK() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		when(productService.updateAllById(any(ProductFromCatalog.class), anyInt())).thenReturn(new CartCountDTO());
		mvc.perform(patch("/products/1").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@.cartsChanged").isNumber());
	}

	@Test
	public void PATCH_products_NOT_FOUND() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		when(productService.updateAllById(any(ProductFromCatalog.class), anyInt()))
				.thenThrow(new EntityNotFoundException("test string"));

		mvc.perform(patch("/products/6").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void PATCH_products_BAD_REQUEST_BODY() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/products/6").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.errorCount").isNumber())
				.andExpect(jsonPath("@.errors").isMap());
		verify(productService, never()).updateAllById(any(ProductFromCatalog.class), anyInt());
	}

	@Test
	public void PATCH_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = productFromCatalog(1, "test", null, 15);
		String json = mapper.writeValueAsString(product);
		mvc.perform(patch("/products/BADPATHVARIABLE").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void DELETE_products_OK() throws Exception {
		when(productService.deleteAllById(1)).thenReturn(new CartCountDTO());
		mvc.perform(delete("/products/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@.cartsChanged").isNumber());
	}

	@Test
	public void DELETE_products_NOT_FOUND() throws Exception {
		when(productService.deleteAllById(anyInt()))
				.thenThrow(new EntityNotFoundException("test string"));
		mvc.perform(delete("/products/6"))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("@.timestamp").isString())
		.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void DELETE_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		mvc.perform(delete("/products/BADPATHVARIABLE"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("@.timestamp").isString())
				.andExpect(jsonPath("@.msg").isString());
	}

	@Test
	public void GET_Allproducts() throws Exception{
		UUID cartId = UUID.randomUUID();
		List<Product> list =  toList(
			productDto(1, 1, "Honda", "Car", cartId, 50, 1),
			productDto(2, 1, "Chips", "Groceries", cartId, 8, 2),
			productDto(3, 1, "Bacon", "Meat", cartId, 15, 9));;
		when(productService.findAllProductsSortedByPrice()).thenReturn(list);

		mvc.perform(get("/products/")).andExpect(status().isOk())
					.andExpect(jsonPath("@[0].catalogId").isNumber())
					.andExpect(jsonPath("@[0].name").isString())
					.andExpect(jsonPath("@[0].description").isString())
					.andExpect(jsonPath("@[0].price").isNumber())
					.andExpect(jsonPath("@[0].quantity").isNumber());;

	}

}
