drop table if exists stock CASCADE;
drop table if exists stock_exchange_mapping CASCADE;
drop table if exists stock_exchange CASCADE;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table stock (id bigint not null, current_price double, description varchar(255), last_update timestamp, name varchar(255), primary key (id));
create table stock_exchange_mapping (stock_exchange_id bigint not null, stock_id bigint not null, primary key (stock_exchange_id, stock_id));
create table stock_exchange (id bigint not null, description varchar(255), name varchar(255), primary key (id));

drop table if exists users CASCADE;
drop table if exists users_roles CASCADE;
drop table if exists roles CASCADE;
create table users (id bigint not null, email varchar(255), password varchar(255), primary key (id), unique(email));
--ALTER TABLE users ADD CONSTRAINT users_email_unique UNIQUE(email);
create table roles (id bigint not null, name varchar(50), primary key (id));
create table users_roles (users_id bigint not null, roles bigint not null, primary key (users_id, roles));