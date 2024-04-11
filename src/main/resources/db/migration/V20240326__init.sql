create table if not exists wookie_user
(
    id            varchar(255) not null primary key,
    email         varchar(255),
    password      varchar(255),
    pseudonym     varchar(255)
);

create table if not exists book
(
    id            varchar(255) not null primary key,
    title         varchar(255),
    description   varchar(255),
    cover         varchar(255),
    price         numeric(38, 2),
    user_id       varchar(255),
    constraint    FK_wookie_user_id foreign key (user_id) references wookie_user(id)
);

create index if not exists INDEX_user_id on book (user_id);


