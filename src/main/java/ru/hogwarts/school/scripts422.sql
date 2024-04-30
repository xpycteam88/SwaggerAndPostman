CREATE TABLE car(
    id BIGINT PRIMARY KEY,
    brand VARCHAR (100),
    model VARCHAR (100),
    price NUMERIC
);

CREATE TABLE person(
    id BIGINT,
    name VARCHAR (100) PRIMARY KEY,
    age INTEGER,
    lows BOOLEAN,
    car_id BIGINT REFERENCES car (id)
);