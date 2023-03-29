package com.gfttraining.cart.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gfttraining.cart.jpa.model.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {}
