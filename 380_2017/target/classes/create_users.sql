CREATE TABLE users (

username VARCHAR(50) NOT NULL,

password VARCHAR(100) NOT NULL,

PRIMARY KEY (username)

);

CREATE TABLE user_roles (

user_role_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),

username VARCHAR(50) NOT NULL,

role VARCHAR(50) NOT NULL,

PRIMARY KEY (user_role_id),

FOREIGN KEY (username) REFERENCES users(username)

);


insert into users values('admin', '$2a$10$oqKGYYBEydAmuB5xeLEaOO5aCBxoSBGHAWw3IzD/Fvf9ZPb3lV4bW');
insert into user_roles(username,role) values ('admin', 'ROLE_ADMIN');
insert into users values('user1', '$2a$10$oqKGYYBEydAmuB5xeLEaOO5aCBxoSBGHAWw3IzD/Fvf9ZPb3lV4bW');
insert into user_roles(username,role) values ('user1', 'ROLE_ADMIN');
insert into users values('user2', '$2a$10$oqKGYYBEydAmuB5xeLEaOO5aCBxoSBGHAWw3IzD/Fvf9ZPb3lV4bW');
insert into user_roles(username,role) values ('user2', 'ROLE_ADMIN');
insert into users values('user3', '$2a$10$oqKGYYBEydAmuB5xeLEaOO5aCBxoSBGHAWw3IzD/Fvf9ZPb3lV4bW');
insert into user_roles(username,role) values ('user3', 'ROLE_ADMIN');
