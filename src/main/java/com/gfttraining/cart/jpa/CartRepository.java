package com.gfttraining.cart.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gfttraining.cart.jpa.model.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {

	@Query("from CartEntity c where c.status=:status")
	List<CartEntity> findByStatus(String status);

	@Query("from CartEntity c where c.userId=:id and c.status='SUBMITTED'")
	List<CartEntity> findByUserId(int id);
	
}


