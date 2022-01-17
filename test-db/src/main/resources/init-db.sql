INSERT INTO order_product (order_id, shipper) VALUES (1, 'Savushkin Product');
INSERT INTO order_product (order_id, shipper) VALUES (2, 'Perfect');
INSERT INTO order_product (order_id, shipper) VALUES (3, 'Belita');

INSERT INTO product (product_id, product_name, product_value, amount, price, order_id) VALUES (1, 'Cheese', '300 gr', 100, 7.55, 1);
INSERT INTO product (product_id, product_name, product_value, amount, price, order_id) VALUES (2, 'Butter', '180 gr', 50, 3.49, 1);
INSERT INTO product (product_id, product_name, product_value, amount, price, order_id) VALUES (3, 'Milk', '1000 ml', 50, 1.69, 1);
INSERT INTO product (product_id, product_name, product_value, amount, price, order_id) VALUES (4, 'White bread', '550 gr', 25, 1.05, 2);
INSERT INTO product (product_id, product_name, product_value, amount, price, order_id) VALUES (5, 'Dark bread', '550 gr', 25, 1.09, 2);

--INSERT INTO order_product (order_id, shipper, date) VALUES (1, 'Savushkin Product', '2021-10-17');
--INSERT INTO order_product (order_id, shipper, date) VALUES (2, 'Perfect', '2021-09-21');
--INSERT INTO order_product (order_id, shipper, date) VALUES (3, 'Belita', '2021-09-21');