-- DROP SCHEMA IF EXISTS air_port_example;

CREATE SCHEMA IF NOT EXISTS air_port_example AUTHORIZATION postgres;

CREATE TABLE IF NOT EXISTS users (
    id              serial primary key,
    username        varchar(250) NOT NULL,
    email           varchar(60) NOT NULL,
    password        varchar(65) NOT NULL, /* hashed value has 60 size */
    created timestamp without time zone NOT NULL
        DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);

CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS users_unique_email_idx on users (email);


CREATE TABLE IF NOT EXISTS airports (
    id              serial primary key,
    name            varchar(250) NOT NULL,
    code            varchar(25) NOT NULL,
    city            varchar(250) NOT NULL,
    user_id         integer,
    created timestamp without time zone NOT NULL
        DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);
ALTER TABLE airports DROP CONSTRAINT IF EXISTS fk_users_airports_id;

alter table airports add constraint fk_users_airports_id
    foreign key (user_id)
        REFERENCES users (id);

CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS airports_unique_code_idx on airports (code);

CREATE INDEX CONCURRENTLY IF NOT EXISTS airports_name_idx on airports (name);
