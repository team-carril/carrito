package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;

public class RestServiceTest extends BaseTestWithConstructors {

	@Mock
	RestTemplate restTemplate;
	@InjectMocks
	RestService service;

	static private final String TEST_URL = "http://MOCKURL.MOCK";

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@Disabled
	public void fetchUser() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + id, User.class)).thenReturn(userResponse(id));
		User actual = service.fetchUserInfo(id);
		assertEquals(id, actual.getId());
		verify(restTemplate).getForEntity(TEST_URL, User.class);
	}

	@Test
	@Disabled
	public void fetchProduct() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + id, ProductFromCatalog.class)).thenReturn(productResponse(id));
		ProductFromCatalog actual = service.fetchProductFromCatalog(id);
		assertEquals(id, actual.getId());
		verify(restTemplate).getForEntity(TEST_URL, ProductFromCatalog.class);
	}

	// TODO if missing key data, THROW
	// TODO if missing entity, THROW

	private ResponseEntity<User> userResponse(int userId) {
		return new ResponseEntity<>(userDTO(userId), HttpStatus.OK);
	}

	private ResponseEntity<ProductFromCatalog> productResponse(int catalogId) {
		return new ResponseEntity<>(productFromCatalog(catalogId, 5, 5), HttpStatus.OK);
	}
}
