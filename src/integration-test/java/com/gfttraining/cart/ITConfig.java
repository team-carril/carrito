package com.gfttraining.cart;

import java.util.UUID;

public class ITConfig {
	// Schemas
	public static final String CART_COUNT_SCHEMA = "schema/cart_count.json";
	public static final String CART_ARRAY_SCHEMA = "schema/carts_array.json";
	public static final String CART_SCHEMA = "schema/cart.json";
	public static final String PRODUCT_SCHEMA = "schema/product.json";
	public static final String PRODUCT_ARRAY_SCHEMA = "schema/products_array.json";
	public static final String BASE_ERROR_SCHEMA = "schema/base_error.json";
	public static final String VALIDATION_ERROR_SCHEMA = "schema/validation_error.json";

	// CartEndPointIT
	public static final UUID CARTa_ID = UUID.fromString("6e7baf09-9877-4c26-a4fb-21e0e2572819");
	public static final UUID CARTb_ID = UUID.fromString("0f6f7a4f-1097-4624-8973-ae6966728647");
	public static final UUID CARTc_ID = UUID.fromString("f92f3177-78cd-4e5d-9324-1954cae5e016");
	public static final UUID CARTd_ID = UUID.fromString("49ea83b7-ad55-449b-8d90-4c0a33a83a73");
	public static final UUID CART_DELETE_ID = UUID.fromString("7e2bb8f9-6bbc-4bc4-915f-f72cb21b035f");
	public static final UUID CART_NOTFOUND_ID = UUID.fromString("163f923b-c48e-44f7-8428-c488a9ef23e5");
	public static final UUID CART_SUBMITTED_ID = UUID.fromString("0aa4e49a-dbba-4a9e-a162-c3a9dc350f7f");

	// ProductEndPointIT
	public static final Integer PRODUCTa_ID = 2;
	public static final Integer PRODUCTa_RESULT = 1;
	public static final Integer PRODUCTb_ID = 10;
	public static final Integer PRODUCTb_RESULT = 1;
	public static final Integer PRODUCT_NOTFOUND_ID = 777;

}
