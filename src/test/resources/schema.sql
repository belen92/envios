DROP TABLE IF EXISTS shipping_item;
DROP TABLE IF EXISTS shipping;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;

CREATE TABLE customer
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    address    VARCHAR(255),
    city       VARCHAR(100)
);

CREATE TABLE product
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    price DECIMAL      NOT NULL
);

CREATE TABLE shipping
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT      NOT NULL,
    send_date   DATE        NOT NULL,
    state       VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE shipping_item
(
    id            BIGSERIAL PRIMARY KEY,
    shipping_id   BIGINT NOT NULL,
    product_id    BIGINT NOT NULL,
    product_count INT    NOT NULL,
    FOREIGN KEY (shipping_id) REFERENCES shipping (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);
