CREATE TABLE CART (
	id VARCHAR(20) PRIMARY KEY,
	to_user INT, 
	to_tax_country INT,
	created_at DATE,
	updated_at DATE,
	status VARCHAR(20)
);

CREATE TABLE PRODUCT (
	id INT PRIMARY KEY,
	to_cart VARCHAR(20),
	category VARCHAR(100),
	description TEXT,
	price FLOAT,
	quantity INT,
);

CREATE TABLE TAXCOUNTRY (
	id INT AUTO_INCREMENT PRIMARY KEY,
	country VARCHAR(50),
	tax_rate INT,
);

ALTER TABLE CART ADD FOREIGN KEY (to_tax_country) REFERENCES TAXCOUNTRY(id);
ALTER TABLE PRODUCT ADD FOREIGN KEY (to_cart) REFERENCES CART(id);