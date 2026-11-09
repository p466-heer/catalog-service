CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,
                      isbn VARCHAR(13) NOT NULL UNIQUE,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      price DOUBLE PRECISION NOT NULL
);
