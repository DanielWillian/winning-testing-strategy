CREATE TABLE IF NOT EXISTS orders (
      order_id bigint NOT NULL,
      product varchar(50) NOT NULL,
      status varchar(50) NOT NULL,
      PRIMARY KEY (order_id)
);

INSERT INTO orders (order_id, product, status) VALUES
(1, 'APPLE', 'CREATED'),
(2, 'BANANA', 'CREATED');
