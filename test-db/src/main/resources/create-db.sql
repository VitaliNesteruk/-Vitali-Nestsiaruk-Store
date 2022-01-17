DROP TABLE IF EXISTS order_product;

DROP TABLE IF EXISTS product;

CREATE TABLE order_product(
    order_id INT NOT NULL AUTO_INCREMENT,
    shipper VARCHAR(250) NOT NULL,
    --date Date,
    CONSTRAINT order_product_pk PRIMARY KEY (order_id)
);

CREATE TABLE product(
    product_id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(250) NOT NULL,
    product_value VARCHAR(250) NOT NULL,
    amount INT NOT NULL,
    price DECIMAL(10,2) DEFAULT 0,
    order_id INT NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (product_id),
    CONSTRAINT order_product_product_fk FOREIGN KEY (order_id) REFERENCES order_product(order_id)
);