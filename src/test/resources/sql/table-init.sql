CREATE SCHEMA IF NOT EXISTS test AUTHORIZATION test;

create table if not exists test.user_entity(
    id serial primary key,
    name varchar,
    username varchar,
    password varchar,
    logo varchar,
    role_permission_entities_id integer,
    created_by varchar,
    updated_by varchar,
    created_date timestamp,
    updated_date timestamp
);
