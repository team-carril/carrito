package com.gfttraining.cart.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gfttraining.cart.BaseTestWithConstructors;
import com.gfttraining.cart.jpa.model.CartEntity;

@DataJpaTest
public class CartRepositoryIT extends BaseTestWithConstructors {

	@Autowired
	CartRepository cartRepository;

	@ParameterizedTest
	@MethodSource("statusArguments")
	public void find_by_status_filters(String status) {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		cartRepository
				.saveAndFlush(cartEntity(null, 1, testDate, testDate, "DRAFT", null));
		cartRepository
				.saveAndFlush(cartEntity(null, 1, testDate, testDate, "DRAFT", null));
		cartRepository
				.saveAndFlush(cartEntity(null, 1, testDate, testDate, "SUBMITTED", null));
		cartRepository
				.saveAndFlush(
						cartEntity(null, 1, testDate, testDate, "SUBMITTED", null));
		List<CartEntity> actualList = cartRepository.findByStatus(status);

		for (CartEntity e : actualList)
			//assertTrue(e.getStatus() == status);
			assertSame(status, e.getStatus());
	}

	@BeforeEach
	public void setupData() {
		cartRepository.deleteAll();
	}

	static Stream<Arguments> statusArguments() {
		return Stream.of(
				Arguments.of("DRAFT"),
				Arguments.of("SUBMITTED"));
	}
	
	
	
	@ParameterizedTest
	@MethodSource("statusArguments_GetCartsByUserId")
	public void find_cartsByUserId(Integer userId) {
		LocalDateTime testDate = LocalDateTime.of(1990, 03, 03, 12, 15, 15);
		cartRepository
				.saveAndFlush(cartEntity(null, 44, testDate, testDate, "DRAFT", null));
		cartRepository
				.saveAndFlush(cartEntity(null, 44, testDate, testDate, "DRAFT", null));
		cartRepository
				.saveAndFlush(cartEntity(null, 44, testDate, testDate, "SUBMITTED", null));
		cartRepository
				.saveAndFlush(
						cartEntity(null, 44, testDate, testDate, "SUBMITTED", null));
		List<CartEntity> actualList = cartRepository.findByUserId(userId);

		for (CartEntity e : actualList)
			//assertTrue(e.getUserId() == userId);
			assertEquals(userId.intValue(), e.getUserId());
	}

	
	static Stream<Arguments> statusArguments_GetCartsByUserId() {
		return Stream.of(
				Arguments.of(44),
				Arguments.of(20),
				Arguments.of(4));
	}

}
