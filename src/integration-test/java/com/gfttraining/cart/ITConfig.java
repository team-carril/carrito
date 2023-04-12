package com.gfttraining.cart;

import java.util.UUID;

public class ITConfig {
	// Schemas
	public static final String CART_COUNT_SCHEMA = "schema/cart_count.json";
	public static final String CART_ARRAY_SCHEMA = "schema/carts_array.json";
	public static final String CART_SCHEMA = "schema/cart.json";
	public static final String PRODUCT_SCHEMA = "schema/product.json";
	public static final String BASE_ERROR_SCHEMA = "schema/base_error.json";
	public static final String VALIDATION_ERROR_SCHEMA = "schema/validation_error.json";

	// CartEndPointIT
	public static final UUID CARTa_ID = UUID.fromString("6e7baf09-9877-4c26-a4fb-21e0e2572819");
	public static final UUID CARTb_ID = UUID.fromString("0f6f7a4f-1097-4624-8973-ae6966728647");
	public static final UUID CART_NOTFOUND_ID = UUID.fromString("163f923b-c48e-44f7-8428-c488a9ef23e5");

	// ProductEndPointIT
	public static final Integer PRODUCTa_ID = 2;
	public static final Integer PRODUCTa_RESULT = 1;
	public static final Integer PRODUCTb_ID = 5;
	public static final Integer PRODUCTb_RESULT = 2;
	public static final Integer PRODUCT_NOTFOUND_ID = 777;

}
