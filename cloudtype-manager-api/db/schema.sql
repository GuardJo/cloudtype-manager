-- 사용자 정보
create table user_info
(
    username    varchar(100) primary key,
    password    varchar(100) not null,
    name        varchar(10)  not null,
    create_at   timestamp    not null default current_timestamp,
    modified_at timestamp    not null default current_timestamp
);