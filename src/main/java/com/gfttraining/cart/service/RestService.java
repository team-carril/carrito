package com.gfttraining.cart.service;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.config.ExternalServicesConfiguration;
import com.gfttraining.cart.exception.RemoteServiceException;

@Service
public class RestService {

	ExternalServicesConfiguration config;
	RestTemplate restTemplate;
	static String CATALOG_URL;
	static String USER_URL;

	@Autowired
	public RestService(ExternalServicesConfiguration config) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		this.restTemplate = builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
		CATALOG_URL = config.getCatalogUrl();
		USER_URL = config.getUsersUrl();
	}

	public RestService(RestTemplate restTemplate, String testURL) {
		this.restTemplate = restTemplate;
		CATALOG_URL = testURL;
		USER_URL = testURL;
	}

	public User fetchUserInfo(int userId) throws RemoteServiceException {
		try {
			ResponseEntity<User> res = restTemplate.getForEntity(USER_URL + userId, User.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + USER_URL + "refused");
		}
	}

	public ProductFromCatalog fetchProductFromCatalog(int catalogId) throws RemoteServiceException {
		try {
			ResponseEntity<ProductFromCatalog> res = restTemplate.getForEntity(CATALOG_URL + "id/" + catalogId,
					ProductFromCatalog.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + CATALOG_URL + "id/ refused");
		}
	}

	public void postStockChange(int id, int quantity) throws RemoteServiceException {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> body = new HttpEntity<String>(Integer.toString(quantity), headers);
			restTemplate.exchange(CATALOG_URL + "updateStock/" + id, HttpMethod.PUT, body, Void.class);
		} catch (RestClientException ex) {
			throw new RemoteServiceException("Connection to " + CATALOG_URL + "updateStock/" + id + " refused");
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
