package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.CartCountDTO;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.ProductRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

public class ProductServiceTest extends BaseTestWithConstructors {

	@Mock
	ProductRepository productRepository;
	@Mock
	CartRepository cartRepository;

	Mapper mapper;
	ProductService service;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
		mapper = new Mapper();
		service = new ProductService(productRepository, cartRepository, mapper);
	}

	@Test
	public void throws_when_products_empty() {
		when(productRepository.findByCatalogId(1)).thenReturn(Collections.emptyList());
		assertThrows(EntityNotFoundException.class, () -> service.findByCatalogId(1));
	}

	@Test
	public void throws_when_cart_not_found() {
		UUID id = UUID.randomUUID();
		when(productRepository.findByCatalogId(1)).thenReturn(Collections.singletonList(
				productEntity(1, null, null, id, 0, 0)));
		when(cartRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> service.findByCatalogId(1));
	}

	@Test
	public void throws_when_cart_found_is_submitted() {
		UUID id = UUID.randomUUID();
		when(productRepository.findByCatalogId(1)).thenReturn(Collections.singletonList(
				productEntity(1, null, null, id, 0, 0)));
		when(cartRepository.findById(id)).thenReturn(Optional.of(cartEntity(id, 0, null, null, "SUBMITTED", null, 0)));
		assertThrows(EntityNotFoundException.class, () -> service.findByCatalogId(1));
	}

	@Test
	public void updateAll_calls_service() {
		UUID cartId = UUID.randomUUID();
		CartEntity draftCart = cartEntity(cartId, 0, null, null, "DRAFT", null);
		ProductFromCatalog product = productFromCatalog(1, "C", null, 0);
		List<ProductEntity> entities = toList(
				productEntity(0, 1, "A", "", cartId, 0, 0),
				productEntity(0, 1, "B", "", cartId, 0, 0),
				productEntity(0, 1, "B", "", cartId, 0, 0));

		List<ProductEntity> entitiesUpdated = toList(
				productEntity(0, 1, "C", "", cartId, 0, 0),
				productEntity(0, 1, "C", "", cartId, 0, 0),
				productEntity(0, 1, "C", "", cartId, 0, 0));
		when(productRepository.findByCatalogId(1)).thenReturn(entities);
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(draftCart));
		CartCountDTO counter = service.updateAllById(product, 1);
		verify(productRepository).findByCatalogId(1);
		verify(productRepository).saveAllAndFlush(entitiesUpdated);
		assertEquals(3, counter.getCartsChanged());
	}

	@Test
	public void deleteAll_calls_service() {
		UUID cartId = UUID.randomUUID();
		CartEntity draftCart = cartEntity(cartId, 0, null, null, "DRAFT", null);
		List<ProductEntity> entities = toList(
				productEntity(0, 1, "A", null, cartId, 0, 0),
				productEntity(0, 1, "B", null, cartId, 0, 0),
				productEntity(0, 1, "B", null, cartId, 0, 0));
		when(productRepository.findByCatalogId(1)).thenReturn(entities);
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(draftCart));
		CartCountDTO counter = service.deleteAllById(1);
		verify(productRepository).findByCatalogId(1);
		verify(productRepository).deleteAll(anyList());
		assertEquals(3, counter.getCartsChanged());
	}

	@Test
	public void findAllProducts() {

		UUID cartId = UUID.randomUUID();
		List<Product> entities = toList(
				productDto(1, 1, "A", null, cartId, 5, 1),
				productDto(2, 1, "B", null, cartId, 8, 2),
				productDto(3, 1, "B", null, cartId, 15, 9));

		when(service.findAllProductsSortedByPrice()).thenReturn(entities);

		Assertions.assertThat(entities)
				.extracting(Product::getPrice)
				.contains(BigDecimal.valueOf(15),
						BigDecimal.valueOf(8),
						BigDecimal.valueOf(5))
				.isSorted();

	}

}
