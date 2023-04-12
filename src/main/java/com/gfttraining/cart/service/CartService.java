package com.gfttraining.cart.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.exception.ImpossibleQuantityException;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {

	private CartRepository cartRepository;
	private Mapper mapper;

	public CartService(CartRepository cartRepository, Mapper mapper) {
		this.cartRepository = cartRepository;
		this.mapper = mapper;
	}

	public List<Cart> findAll() {
		List<CartEntity> cartEntityList = cartRepository.findAll();
		return cartEntityList.stream().map((e) -> mapper.toCartDTO(e))
				.sorted(Comparator.comparing(Cart::getStatus)).collect(Collectors.toList());
	}

	public List<Cart> findByStatus(String status) {
		List<CartEntity> entities = cartRepository.findByStatus(status);

		return entities.stream().map((e) -> mapper.toCartDTO(e)).collect(Collectors.toList());
	}

	public Cart postNewCart(User user) {
		CartEntity entity = CartEntity.builder().createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.userId(user.getId())
				.status("DRAFT").products(Collections.emptyList()).build();

		CartEntity result = cartRepository.save(entity);

		log.debug("Id: " + entity.getId() + " User Id: " + entity.getUserId() + " Products: " + entity.getProducts()
				+ " Status: " + entity.getStatus());

		return mapper.toCartDTO(result);
	}

	public Cart addProductToCart(ProductFromCatalog productFromCatalog, UUID cartId) {

		Product product = mapper.toProductDTO(productFromCatalog);

		Optional<CartEntity> entityOptional = cartRepository.findById(cartId);
		if (entityOptional.isEmpty())
			throw new EntityNotFoundException("Cart " + cartId + " not found.");
		CartEntity entity = entityOptional.get();

		Optional<ProductEntity> sameProduct = entity.getProducts().stream()
				.filter(p -> (p.getCatalogId() == product.getCatalogId() && p.getCartId().equals(cartId)))
				.findFirst();
		if (sameProduct.isEmpty()) {
			if (product.getQuantity() < 1)
				throw new ImpossibleQuantityException(
						"Quantity must restult in an integer bigger than 0.");
			entity.getProducts().add(mapper.toProductEntity(product));
			entity = cartRepository.saveAndFlush(entity);
			return mapper.toCartDTO(entity);
		}
		sameProduct.get().addToQuantity(product.getQuantity());
		// if (sameProduct.get().getQuantity() < 1)

		entity = cartRepository.saveAndFlush(entity);

		log.debug("Id: " + entity.getId() + " User Id: " + entity.getUserId() + " Products: " + entity.getProducts()
				+ " Status: " + entity.getStatus());

		return mapper.toCartDTO(entity);
	}

	public Cart deleteById(UUID cartId) {
		Optional<CartEntity> entityOptional = cartRepository.findById(cartId);
		if (entityOptional.isEmpty())
			throw new EntityNotFoundException("Cart " + cartId + " not found.");
		cartRepository.delete(entityOptional.get());

		log.debug(cartId + "deleted");

		return new Cart();
	}
	
	public List<CartEntity> getAllCartEntitiesByUserIdFilteredByStatus(Integer userId){
		List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
		return cartEntities;
	}
}
