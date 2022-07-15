CREATE TABLE payment (
  id    BIGINT PRIMARY KEY,
  price DECIMAL(30, 8) NOT NULL,
  bankResponse VARCHAR(4000)
);

CREATE TABLE product (
  id    BIGINT PRIMARY KEY,
  price DECIMAL(30, 8) NOT NULL,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(4000),
  stock INTEGER NOT NULL
);