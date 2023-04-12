package com.gfttraining.cart.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.jpa.model.ProductEntity;

@DataJpaTest
public class ProductRepositoryIT extends BaseTestWithConstructors {

	@Autowired
	ProductRepository repository;

	@Test
	public void findByCatalogId_filters() {
		List<ProductEntity> entities = repository.findByCatalogId(5);
		for (ProductEntity p : entities)
			assertEquals(5, p.getCatalogId());
	}

	@Test
	public void findByCatalogId_empty_list() {
		List<ProductEntity> entities = repository.findByCatalogId(1000);
		assertTrue(entities.isEmpty());
	}

}
