package com.gfttraining.cart.service;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.exception.RemoteServiceException;

@Service
public class RestService {

	RestTemplate restTemplate;
	static String CATALOG_URL;
	static String USER_URL;

	public RestService() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		this.restTemplate = builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
		CATALOG_URL = "http://localhost:8081/products/";
		USER_URL = "http://localhost:8082/users/bInfo/";
	}

	public RestService(RestTemplate restTemplate, String testURL) {
		this.restTemplate = restTemplate;
		CATALOG_URL = testURL;
		USER_URL = testURL;
	}

	public User fetchUserInfo(int userId) throws RemoteServiceException {
		try {
			ResponseEntity<User> res = restTemplate.getForEntity(CATALOG_URL + "id/" + 1, User.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + CATALOG_URL + "id/ refused");
		}
	}

	public ProductFromCatalog fetchProductFromCatalog(int catalogId) throws RemoteServiceException {
		try {
			ResponseEntity<ProductFromCatalog> res = restTemplate.getForEntity(USER_URL + 1, ProductFromCatalog.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + USER_URL + " refused");
		}
	}

	public void postStockChange(int id, int quantity) throws RemoteServiceException {
		try {
			HttpEntity<String> body = new HttpEntity<String>(Integer.toString(quantity));
			restTemplate.exchange(CATALOG_URL + "updateStock/" + id, HttpMethod.POST, body, Void.class);
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + CATALOG_URL + "updateStock/ refused");
		}

	}

}

class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse res) throws IOException {
		return res.getStatusCode().series() == SERVER_ERROR
				|| res.getStatusCode().series() == CLIENT_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse res) throws IOException {
		if (res.getStatusCode().series() == SERVER_ERROR) {
			throw new RemoteServiceException("Remote service internal error.", res.getStatusCode());
		} else if (res.getStatusCode().series() == CLIENT_ERROR) {
			if (res.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new RemoteServiceException("Remote service could not find resource.", res.getStatusCode());
			if (res.getStatusCode() == HttpStatus.BAD_REQUEST)
				throw new RemoteServiceException("Remote service returned bad request.", res.getStatusCode());
		}
	}
}
