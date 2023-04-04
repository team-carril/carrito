package com.gfttraining.cart.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductEndpointIT {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ProductService productService;

	@Test
	public void PATCH_products_OK() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(1);
		product.setName("test");
		product.setPrice(new BigDecimal(15));
		product.setDescription("");
		String json = mapper.writeValueAsString(product);
		when(productService.updateAllById(any(Product.class), anyInt())).thenReturn(new CartCountDTO());
		mvc.perform(patch("/products/1").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("@.cartsChanged").isNumber());
	}

	@Test
	public void PATCH_products_NOT_FOUND() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(1);
		product.setName("test");
		product.setPrice(new BigDecimal(15));
		product.setDescription("");
		String json = mapper.writeValueAsString(product);
		when(productService.updateAllById(any(Product.class), anyInt()))
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
		verify(productService, never()).updateAllById(any(Product.class), anyInt());
	}

	@Test
	public void PATCH_products_BAD_REQUEST_PATH_VARIABLE() throws Exception {
		ProductFromCatalog product = new ProductFromCatalog();
		product.setId(1);
		product.setName("test");
		product.setPrice(new BigDecimal(15));
		product.setDescription("");
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

}
