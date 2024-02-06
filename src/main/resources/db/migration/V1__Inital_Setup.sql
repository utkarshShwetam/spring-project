CREATE SEQUENCE customer_id_seq;

CREATE TABLE customer(
    id INTEGER DEFAULT nextval('customer_id_seq') PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT CONSTRAINT customer_email_unique UNIQUE NOT NULL,
    age INT NOT NULL
);