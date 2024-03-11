-- V001

create table users (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    cpf VARCHAR(11) NOT NULL,
    email VARCHAR(255) NOT NULL,
    UNIQUE(cpf, email)
);