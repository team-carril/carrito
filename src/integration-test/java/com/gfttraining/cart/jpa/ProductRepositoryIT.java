package com.gfttraining.cart.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Comparator;

import org.assertj.core.api.Assertions;
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

	@Test
	public void findAllAndSortByPrice(){
		repository.saveAndFlush(productEntity(1, 5, null, null, null, 54, 1));
		repository.saveAndFlush(productEntity(2, 5, null, null, null, 15, 13));
		repository.saveAndFlush(productEntity(3, 5, null, null, null, 14, 12412));
		repository.saveAndFlush(productEntity(4, 5, null, null, null, 45, 897));
		repository.saveAndFlush(productEntity(5, 5, null, null, null, 5, 55));
		repository.saveAndFlush(productEntity(6, 5, null, null, null, 1, 74));
		repository.saveAndFlush(productEntity(7, 5, null, null, null, 4, 33));

		List<ProductEntity> entities = repository.findAllAndSortByPrice();
		entities.sort(Comparator.comparing(ProductEntity::getPrice));
		Assertions.assertThat(entities).isSortedAccordingTo(Comparator.comparing(ProductEntity::getPrice));
	}

}
