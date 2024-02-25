create table uzer
(
    id SERIAL PRIMARY KEY,
    username varchar(60) unique not null,
    password varchar(60) not null,
    role varchar(20)
);