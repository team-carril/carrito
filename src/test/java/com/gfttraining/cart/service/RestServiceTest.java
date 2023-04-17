package com.gfttraining.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.exception.RemoteServiceException;

@ExtendWith(MockitoExtension.class)
public class RestServiceTest extends BaseTestWithConstructors {

	static private final String TEST_URL = "http://MOCKURL.MOCK/";

	@Mock
	RestTemplate restTemplate;
	RestService service;

	@BeforeEach
	public void init() {
		service = new RestService(restTemplate, TEST_URL);
	}

	@Test
	public void fetchUser() throws RemoteServiceException {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + "id/" + id, User.class)).thenReturn(userResponse(id));

		User actual = service.fetchUserInfo(id);
		assertEquals(id, actual.getId());
	}

	@Test
	public void fetch_user_fails_server_down() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + "id/" + id, User.class))
				.thenThrow(new ResourceAccessException("asdf"));
		assertThrows(RemoteServiceException.class, () -> service.fetchUserInfo(id));
	}

	@Test
	public void fetch_user_fails_user_not_found() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + "id/" + id, User.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		assertThrows(RemoteServiceException.class, () -> service.fetchUserInfo(id));
	}

	@Test
	public void fetchProduct() throws RemoteServiceException {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + id, ProductFromCatalog.class)).thenReturn(productResponse(id));
		ProductFromCatalog actual = service.fetchProductFromCatalog(id);
		assertEquals(id, actual.getId());
		verify(restTemplate).getForEntity(TEST_URL + id, ProductFromCatalog.class);
	}

	@Test
	public void fetch_product_fails_server_down() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + id, ProductFromCatalog.class))
				.thenThrow(new ResourceAccessException("asdf"));
		assertThrows(RemoteServiceException.class, () -> service.fetchProductFromCatalog(id));
	}

	@Test
	public void fetch_user_fails_product_not_found() {
		int id = 1;
		when(restTemplate.getForEntity(TEST_URL + id, ProductFromCatalog.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		assertThrows(RemoteServiceException.class, () -> service.fetchProductFromCatalog(id));
	}

	@Test
	public void post_stock_fails_server_down() {
		int id = 1;
		HttpEntity<String> body = new HttpEntity<>("5");
		when(restTemplate.exchange(TEST_URL + "updateStock/" + id, HttpMethod.POST, body, Void.class))
				.thenThrow(new ResourceAccessException("asd"));

		assertThrows(RemoteServiceException.class, () -> service.postStockChange(id, 5));
	}

	@Test
	public void post_stock_fails_not_found() {
		int id = 1;
		HttpEntity<String> body = new HttpEntity<>("5");
		when(restTemplate.exchange(TEST_URL + "updateStock/" + id, HttpMethod.POST, body, Void.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

		assertThrows(RemoteServiceException.class, () -> service.postStockChange(id, 5));
	}

	private ResponseEntity<User> userResponse(int userId) {
		return new ResponseEntity<>(userDTO(userId), HttpStatus.OK);
	}

	private ResponseEntity<ProductFromCatalog> productResponse(int catalogId) {
		return new ResponseEntity<>(productFromCatalog(catalogId, 5, 5), HttpStatus.OK);
	}

}
