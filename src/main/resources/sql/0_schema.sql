CREATE TABLE CART (
	id UUID PRIMARY KEY UNIQUE,
	to_user INT,
	created_at DATETIME,
	updated_at DATETIME,
	status VARCHAR(20)
);

CREATE TABLE PRODUCT (
	id INT PRIMARY KEY,
	to_cart UUID,
	name TEXT,
	description TEXT,
	price DECIMAL(10, 2),
	quantity INT
);

ALTER TABLE
	PRODUCT
ADD
	FOREIGN KEY (to_cart) REFERENCES CART(id);