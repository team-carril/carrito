package com.gfttraining.cart.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.controller.ProductController;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.config.FeatureConfiguration;
import com.gfttraining.cart.exception.BadMethodRequestException;
import com.gfttraining.cart.service.ProductService;

class ProductControllerTest extends BaseTestWithConstructors {

	@Mock
	ProductService productService;

	@Mock
	FeatureConfiguration featureConfiguration;

	@InjectMocks
	ProductController productController;

	static int catalogId = 1;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void updateAll_calls_service() throws BadMethodRequestException {
		ProductFromCatalog productFromCatalog = new ProductFromCatalog();
		productFromCatalog.setPrice(new BigDecimal(7));
		when(featureConfiguration.getUpdateAllByIdEnabled()).thenReturn(true);

		productController.updateAllById(productFromCatalog, catalogId);

		ArgumentCaptor<ProductFromCatalog> productCaptor = ArgumentCaptor.forClass(ProductFromCatalog.class);
		ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(productService).updateAllById(productCaptor.capture(), idCaptor.capture());

		assertEquals(productFromCatalog, productCaptor.getValue());
		assertEquals(catalogId, idCaptor.getValue().intValue());

	}

	@Test
	void deleteAll_calls_service() throws BadMethodRequestException {
		productController.deleteAllById(catalogId);

		verify(productService).deleteAllById(1);
	}

	@Test
	void findAllProductsSortedByPrice() {
		UUID cartId = UUID.randomUUID();
		List<Product> expectedProducts = toList(
				productDto(1, 1, "Bread", "Ordinary bread", cartId, 5, 1),
				productDto(2, 1, "Sausage", "Original product", cartId, 8, 2),
				productDto(3, 1, "Milk", "99% Real milk", cartId, 15, 9));

		when(productService.findAllProductsSortedByPrice()).thenReturn(expectedProducts);
		List<Product> actualProducts = productController.findAllProductsSortedByPrice();

		verify(productService).findAllProductsSortedByPrice();
		assertEquals(expectedProducts, actualProducts);
	}

	@Test
	void updateAll_calls_service_false() throws BadMethodRequestException {
		ProductFromCatalog productFromCatalog = new ProductFromCatalog();
		productFromCatalog.setPrice(new BigDecimal(7));

		when(featureConfiguration.getUpdateAllByIdEnabled()).thenReturn(false);
		BadMethodRequestException exception = assertThrows(BadMethodRequestException.class,
				() -> productController.updateAllById(productFromCatalog, catalogId));
		verify(featureConfiguration).getUpdateAllByIdEnabled();
		assertEquals("Feature Flag is Disable", exception.getMessage());
	}

}
