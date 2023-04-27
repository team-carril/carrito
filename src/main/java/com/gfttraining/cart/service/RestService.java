package com.gfttraining.cart.service;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.cart.api.dto.ProductFromCatalog;
import com.gfttraining.cart.api.dto.User;
import com.gfttraining.cart.config.ExternalServicesConfiguration;
import com.gfttraining.cart.exception.RemoteServiceBadRequestException;
import com.gfttraining.cart.exception.RemoteServiceInternalException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Retryable
	public User fetchUserInfo(int userId) throws RemoteServiceInternalException {
		try {
			log.info("Retrieving from user service, user id: {}", userId);
			ResponseEntity<User> res = restTemplate.getForEntity(USER_URL + userId, User.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceInternalException("Connection to " + USER_URL + "refused");
		}
	}

	@Retryable
	public ProductFromCatalog fetchProductFromCatalog(int catalogId) throws RemoteServiceInternalException {
		try {
			log.info("Retrieving from catalog, product id: {}", catalogId);
			ResponseEntity<ProductFromCatalog> res = restTemplate.getForEntity(CATALOG_URL + "id/" + catalogId,
					ProductFromCatalog.class);
			return res.getBody();
		} catch (RestClientException ex) {
			throw new RemoteServiceInternalException("Connection to " + CATALOG_URL + "id/ refused");
		}
	}

	@Retryable
	public void postStockChange(int id, int quantity) throws RemoteServiceInternalException {
		try {
			log.info("Sending changes in stock ({} items) to catalog service. Product: {}", quantity, id);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> body = new HttpEntity<String>(Integer.toString(quantity), headers);
			restTemplate.exchange(CATALOG_URL + "updateStock/" + id, HttpMethod.PUT, body, Void.class);
		} catch (RestClientException ex) {
			throw new RemoteServiceInternalException("Connection to " + CATALOG_URL + "updateStock/" + id + " refused");
		}

	}

}

class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse res) throws IOException {
		return res.getStatusCode().series() == CLIENT_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse res) throws IOException {
		if (res.getStatusCode().series() == CLIENT_ERROR) {
			if (res.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new EntityNotFoundException("Remote service could not find resource.");
			if (res.getStatusCode() == HttpStatus.BAD_REQUEST)
				throw new RemoteServiceBadRequestException("Remote service returned bad request.");
		}
	}
}
