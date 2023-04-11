package com.gfttraining.cart.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.jpa.model.ProductEntity;

@DataJpaTest
public class ProductRepositoryIT extends BaseTestWithConstructors {

	@Autowired
	ProductRepository repository;

	@Test
	public void findByCatalogId_filters() {
		repository.saveAndFlush(productEntity(1, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(2, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(3, 3, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(4, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(5, 4, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(5, 2, null, null, null, 0, 0));

		List<ProductEntity> entities = repository.findByCatalogId(5);

		for (ProductEntity p : entities)
			assertEquals(5, p.getCatalogId());
	}

	@Test
	public void findByCatalogId_empty_list() {
		repository.saveAndFlush(productEntity(1, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(2, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(3, 3, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(4, 5, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(5, 4, null, null, null, 0, 0));
		repository.saveAndFlush(productEntity(5, 2, null, null, null, 0, 0));

		List<ProductEntity> entities = repository.findByCatalogId(1);
		assertTrue(entities.isEmpty());
	}

	@Test
	public void findAllAndSortByPrice(){
		repository.saveAndFlush(productEntity(1, 5, null, null, null, 0, 0));

		List<ProductEntity> entities = repository.findAllAndSortByPrice();

		assertTrue(!entities.isEmpty());
	}

}
