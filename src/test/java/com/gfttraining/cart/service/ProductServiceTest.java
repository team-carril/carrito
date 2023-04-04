package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.jpa.ProductRepository;
import com.gfttraining.cart.jpa.model.ProductEntity;

public class ProductServiceTest extends BaseTestWithConstructors {

	@Mock
	ProductRepository productRepository;
	@InjectMocks
	ProductService service;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void throws_when_empty() {
		when(productRepository.findByCatalogId(1)).thenReturn(Collections.emptyList());
		assertThrows(EntityNotFoundException.class, () -> service.findByCatalogId(1));
	}

	@Test
	public void updateAll_calls_service() {
		Product product = productDto(0, 1, "C", null, null, 0, 0);
		List<ProductEntity> entities = toList(
				productEntity(0, 1, "A", null, null, 0, 0),
				productEntity(0, 1, "B", null, null, 0, 0),
				productEntity(0, 1, "B", null, null, 0, 0));

		List<ProductEntity> entitiesUpdated = toList(
				productEntity(0, 1, "C", null, null, 0, 0),
				productEntity(0, 1, "C", null, null, 0, 0),
				productEntity(0, 1, "C", null, null, 0, 0));
		when(productRepository.findByCatalogId(1)).thenReturn(entities);
		CartCountDTO counter = service.updateAllById(product, 1);
		verify(productRepository).findByCatalogId(1);
		verify(productRepository).saveAllAndFlush(entitiesUpdated);
		assertEquals(3, counter.getCartsChanged());
	}

	@Test
	public void deleteAll_calls_service() {
		List<ProductEntity> entities = toList(
				productEntity(0, 1, "A", null, null, 0, 0),
				productEntity(0, 1, "B", null, null, 0, 0),
				productEntity(0, 1, "B", null, null, 0, 0));
		when(productRepository.findByCatalogId(1)).thenReturn(entities);
		CartCountDTO counter = service.deleteAllById(1);
		verify(productRepository).findByCatalogId(1);
		verify(productRepository).deleteAll(anyList());
		assertEquals(3, counter.getCartsChanged());
	}

}
