package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.Cart;
import com.gfttraining.cart.api.dto.Product;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.config.RatesConfiguration;
import com.gfttraining.cart.exception.InvalidUserDataException;
import com.gfttraining.cart.exception.OutOfStockException;
import com.gfttraining.cart.exception.RemoteServiceException;
import com.gfttraining.cart.jpa.CartRepository;
import com.gfttraining.cart.jpa.model.CartEntity;
import com.gfttraining.cart.jpa.model.ProductEntity;

public class CartServiceTest extends BaseTestWithConstructors {

	@Mock
	CartRepository cartRepository;
	@Mock
	RestService restService;
	Mapper mapper;
	RatesConfiguration ratesConfig;
	CartService cartService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		mapper = new Mapper();
		ratesConfig = initTestRatesConfig();
		cartService = new CartService(cartRepository, mapper, restService, ratesConfig);
	}

	@Test
	public void findAll_calls_repository() {
		cartService.findAll();
		verify(cartRepository).findAll();
	}

	@Test
	public void handles_empty() {
		when(cartRepository.findAll()).thenReturn(Collections.emptyList());
		cartService.findAll();
		verify(cartRepository).findAll();
	}

	@Test
	public void returns_sorted_dtos() {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		UUID uuidA = UUID.randomUUID();
		UUID uuidB = UUID.randomUUID();
		UUID uuidC = UUID.randomUUID();

		List<ProductEntity> p1 = toList(productEntity(1, 1, "test_item", "asdf", uuidA, 5, 1),
				productEntity(2, 2, "test_item", "asdf", uuidA, 5, 1),
				productEntity(3, 3, "test_item", "asdf", uuidA, 5, 1));
		List<Product> p2 = toList(productDto(1, 1, "test_item", "asdf", uuidA, 5, 1),
				productDto(2, 2, "test_item", "asdf", uuidA, 5, 1),
				productDto(3, 3, "test_item", "asdf", uuidA, 5, 1));

		List<CartEntity> listInput = toList(
				cartEntity(uuidA, 1, testDate, testDate, "DRAFT", p1),
				cartEntity(uuidB, 1, testDate, testDate, "SUBMITTED", p1),
				cartEntity(uuidC, 1, testDate, testDate, "DRAFT", p1));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", p2, 15),
				cartDto(uuidC, 1, testDate, testDate, "DRAFT", p2, 15),
				cartDto(uuidB, 1, testDate, testDate, "SUBMITTED", p2, 15));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);
	}

	@ParameterizedTest
	@MethodSource("statusArguments")
	public void returns_filtered_by_status(String status) {
		cartService.findByStatus(status);
		verify(cartRepository).findByStatus(status);
	}

	@Test
	public void handles_empty_product_list() {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		UUID uuidA = UUID.randomUUID();

		List<CartEntity> listInput = toList(cartEntity(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList()));
		List<Cart> listExpected = toList(
				cartDto(uuidA, 1, testDate, testDate, "DRAFT", Collections.emptyList(), 0));
		when(cartRepository.findAll()).thenReturn(listInput);
		List<Cart> listActual = cartService.findAll();
		assertEquals(listExpected, listActual);
	}

	@Test
	public void create_calls_repository() {
		User user = userDTO(1);
		when(cartRepository.save(any(CartEntity.class)))
				.thenReturn(cartEntity(null, 0, null, null, null, Collections.emptyList()));
		cartService.postNewCart(user);
		verify(cartRepository).save(any(CartEntity.class));
	}

	@Test
	public void add_product_existing_product() {
		UUID uuid = UUID.randomUUID();
		ProductFromCatalog product = productFromCatalog(1, null, null, 0);
		CartEntity entity = cartEntity(uuid, 0, null, null, null,
				toList(productEntity(1, 1, null, null, uuid, 0, 1)));

		when(cartRepository.findById(uuid)).thenReturn(Optional.of(entity));
		when(cartRepository.saveAndFlush(entity)).thenReturn(entity);
		cartService.addProductToCart(product, uuid);
		verify(cartRepository).findById(uuid);
		verify(cartRepository).saveAndFlush(entity);
	}

	@Test
	@Disabled // TODO
	public void add_product_new_product() {
	}

	@Test
	public void add_product_throws_cart_not_found() {
		UUID uuid = UUID.randomUUID();
		when(cartRepository.findById(uuid)).thenReturn(Optional.ofNullable(null));
		assertThrows(EntityNotFoundException.class, () -> cartService.addProductToCart(new ProductFromCatalog(), uuid));
	}

	@Test
	public void delete_product_OK() {
		UUID id = UUID.randomUUID();
		when(cartRepository.findById(id)).thenReturn(Optional.of(new CartEntity()));
		cartService.deleteById(id);
		verify(cartRepository).delete(any(CartEntity.class));
	}

	@Test
	public void delete_product_throws_NOT_FOUND() {
		UUID id = UUID.randomUUID();
		when(cartRepository.findById(id)).thenReturn(Optional.ofNullable(null));
		assertThrows(EntityNotFoundException.class, () -> cartService.deleteById(id));
	}

	@DisplayName("GIVEN a CartEntity in DB and valid info in microservices WHEN service is called SHOULD Transform Properly")
	@Test
	public void validateCart() throws RemoteServiceException, OutOfStockException, InvalidUserDataException {
		// GIVEN this CartEntity in DB
		UUID id = UUID.randomUUID();
		List<ProductEntity> productsInDB = toList(
				productEntity(2, null, null, id, 10, 2),
				productEntity(3, null, null, id, 5, 2),
				productEntity(4, null, null, id, 20, 2));
		CartEntity entity = cartEntity(id, 7, null, null, "DRAFT", productsInDB);
		when(cartRepository.findById(id)).thenReturn(Optional.of(entity));
		// GIVEN this new information from Catalog microservice
		List<ProductFromCatalog> productsFromCatalog = toList(
				productFromCatalog(2, 10, 10),
				productFromCatalog(3, 10, 10),
				productFromCatalog(4, 10, 10));
		when(restService.fetchProductFromCatalog(anyInt())).thenReturn(productsFromCatalog.get(0))
				.thenReturn(productsFromCatalog.get(1))
				.thenReturn(productsFromCatalog.get(2));
		// GIVEN this information from User microservice
		when(restService.fetchUserInfo(entity.getUserId())).thenReturn(userDTO(7, "VISA", "SPAIN"));
		// SHOULD transform to
		List<ProductEntity> expectedProducts = toList(
				productEntity(2, null, null, id, 10, 2),
				productEntity(3, null, null, id, 10, 2),
				productEntity(4, null, null, id, 10, 2));
		CartEntity expectedEntity = cartEntity(id, 7, null, null, "SUBMITTED", expectedProducts, 72.6);
		when(cartRepository.saveAndFlush(any(CartEntity.class))).thenReturn(expectedEntity);

		// WHEN service is called
		cartService.validateCart(id);

		verify(cartRepository).saveAndFlush(expectedEntity);
		verify(restService, times(entity.getProducts().size())).fetchProductFromCatalog(anyInt());
		verify(restService, times(entity.getProducts().size())).postStockChange(anyInt(), anyInt());
	}

	static Stream<Arguments> statusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"));
	}

	@Test
	public void get_Carts_By_UserId_OK() {
		List<CartEntity> entities = Collections.emptyList();
		when(cartRepository.findByUserId(1)).thenReturn(entities);
		cartService.getAllCartEntitiesByUserIdFilteredByStatus(1);
		verify(cartRepository).findByUserId(1);
	}
}
