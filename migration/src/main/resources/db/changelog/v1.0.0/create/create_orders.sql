CREATE TABLE IF NOT EXISTS orders
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE NOT NULL,
    restaurant_id BIGINT REFERENCES restaurant(id) ON DELETE CASCADE NOT NULL,
    courier_id BIGINT REFERENCES courier(id) ON DELETE SET NULL,
    status VARCHAR(100) NOT NULL,
    order_date TIMESTAMP
);