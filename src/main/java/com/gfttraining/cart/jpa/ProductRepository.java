package com.gfttraining.cart.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gfttraining.cart.jpa.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	@Query("from ProductEntity p where p.catalogId=:catalogId")
	List<ProductEntity> findByCatalogId(int catalogId);

}
