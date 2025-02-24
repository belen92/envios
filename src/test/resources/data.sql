INSERT INTO customer (first_name, last_name, address, city)
VALUES ('Juan', 'Pérez', 'Calle Falsa 123', 'Ciudad A'),
       ('María', 'González', 'Av. Siempre Viva', 'Ciudad B');

INSERT INTO product (name, price)
VALUES ('Laptop', 1500.00),
       ('Mouse', 25.00);

INSERT INTO shipping (customer_id, send_date, state)
VALUES (1, '2025-02-24', 'INICIAL');

INSERT INTO shipping_item (shipping_id, product_id, product_count)
VALUES (1, 1, 2),
       (1, 2, 5);
INSERT INTO product (name, description)
VALUES ('Product 1', 'Description 1');
INSERT INTO product (name, description)
VALUES ('Product 2', 'Description 2');

INSERT INTO customer (first_name, last_name, address, city)
VALUES ('John', 'Doe', '123 Main St', 'Springfield');

INSERT INTO shipping (send_date, state, customer_id)
VALUES ('2025-02-24', 'INICIAL', 1);

INSERT INTO shipping_item (product_id, product_count, shipping_id)
VALUES (1, 10, 1);
INSERT INTO shipping_item (product_id, product_count, shipping_id)
VALUES (2, 5, 1);
