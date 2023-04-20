package com.gfttraining.cart.service;

import java.math.BigDecimal;
import java.math.MathContext;
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
import com.gfttraining.cart.config.RatesConfiguration;
import com.gfttraining.cart.exception.ImpossibleQuantityException;
import com.gfttraining.cart.exception.InvalidUserDataException;
import com.gfttraining.cart.exception.OutOfStockException;
import com.gfttraining.cart.exception.RemoteServiceInternalException;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {

	private CartRepository cartRepository;
	private Mapper mapper;
	private RestService restService;
	private RatesConfiguration ratesConfig;

	public CartService(CartRepository cartRepository, Mapper mapper, RestService restService,
			RatesConfiguration ratesConfig) {
		this.cartRepository = cartRepository;
		this.mapper = mapper;
		this.restService = restService;
		this.ratesConfig = ratesConfig;
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

		CartEntity entity = findById(cartId);

		Optional<ProductEntity> sameProduct = entity.getProducts().stream()
				.filter(p -> (p.getCatalogId() == product.getCatalogId() && p.getCartId().equals(cartId)))
				.findFirst();
		if (sameProduct.isEmpty()) {
			if (product.getQuantity() < 1)
				throw new ImpossibleQuantityException(
						"Quantity must restult in an integer bigger than 0.");
			entity.getProducts().add(mapper.toProductEntity(product));
			entity.setUpdatedAt(LocalDateTime.now());
			entity = cartRepository.saveAndFlush(entity);

			log.debug("Id: " + entity.getId() + " User Id: " + entity.getUserId() + " Products: " + entity.getProducts()
					+ " Status: " + entity.getStatus());
			return mapper.toCartDTO(entity);
		}
		sameProduct.get().addToQuantity(product.getQuantity());
		entity.setUpdatedAt(LocalDateTime.now());
		entity = cartRepository.saveAndFlush(entity);

		log.debug("Id: " + entity.getId() + " User Id: " + entity.getUserId() + " Products: " + entity.getProducts()
				+ " Status: " + entity.getStatus());

		return mapper.toCartDTO(entity);
	}

	public Cart deleteById(UUID cartId) {

		CartEntity entity = findById(cartId);
		cartRepository.delete(entity);

		log.debug(cartId + "deleted");

		return new Cart();
	}

	public List<Cart> getAllCartEntitiesByUserIdFilteredByStatus(Integer userId) {
		List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
		return cartEntities.stream().map((e) -> mapper.toCartDTO(e)).collect(Collectors.toList());
	}

	public Cart validateCart(UUID cartId)
			throws RemoteServiceInternalException, OutOfStockException, InvalidUserDataException {

		CartEntity entity = findById(cartId);
		if (entity.getStatus().equals("SUBMITTED"))
			throw new EntityNotFoundException("Cart " + cartId + "is already submitted.");

		User user = restService.fetchUserInfo(entity.getUserId());
		validateUserInformation(user);
		for (ProductEntity p : entity.getProducts()) {
			ProductFromCatalog productFromCatalog = restService.fetchProductFromCatalog(p.getCatalogId());
			if (productFromCatalog.getStock() < p.getQuantity())
				throw new OutOfStockException(
						"Product with catalogId: " + p.getCatalogId() + "out of stock. Cart not submitted.");
			if (productFromCatalog.getPrice() != p.getPrice())
				p.setPrice(productFromCatalog.getPrice());
		}

		BigDecimal priceAfterRates = calculatePriceAfterRates(
				entity.calculatePrice(),
				ratesConfig.getPaymentMethod().get(user.getPaymentMethod()),
				ratesConfig.getCountry().get(user.getCountry()));
		entity.setTotalPrice(priceAfterRates);

		for (ProductEntity p : entity.getProducts())
			restService.postStockChange(p.getCatalogId(), p.getQuantity());

		entity.setStatus("SUBMITTED");
		entity.setUpdatedAt(LocalDateTime.now());
		entity = cartRepository.saveAndFlush(entity);

		return mapper.toCartDTO(entity);
	}

	private BigDecimal calculatePriceAfterRates(BigDecimal cartPrice, int paymentMethodRate, int countryRate) {
		cartPrice = applyRate(cartPrice, paymentMethodRate);
		cartPrice = applyRate(cartPrice, countryRate);
		cartPrice = cartPrice.round(new MathContext(4));
		return cartPrice;
	}

	private BigDecimal applyRate(BigDecimal p, int configRate) {
		BigDecimal rate = new BigDecimal(configRate).divide(new BigDecimal(100));
		p = p.add(rate.multiply(p));
		return p.stripTrailingZeros();
	}

	private CartEntity findById(UUID cartId) {
		Optional<CartEntity> entityOptional = cartRepository.findById(cartId);
		if (entityOptional.isEmpty())
			throw new EntityNotFoundException("Cart " + cartId + " not found.");
		return entityOptional.get();
	}

	private void validateUserInformation(User user) throws InvalidUserDataException {
		if (!ratesConfig.getPaymentMethod().containsKey(user.getPaymentMethod()))
			throw new InvalidUserDataException("Unrecognized payment method: " + user.getPaymentMethod());

		if (!ratesConfig.getCountry().containsKey(user.getCountry()))
			throw new InvalidUserDataException("Unrecognized country: " + user.getCountry());

	}
}
