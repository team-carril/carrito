package com.gfttraining.cart.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gfttraining.cart.jpa.model.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, String> {}
