-- 관리 서버 ID sequence
create sequence server_info_id_seq;

-- 사용자 정보
create table user_info
(
    username    varchar(100) primary key,
    password    varchar(100) not null,
    name        varchar(10)  not null,
    created_at  timestamp    not null default current_timestamp,
    modified_at timestamp    not null default current_timestamp
);

-- 관리 서버 정보
create table server_info
(
    id               bigint primary key    default nextval('server_info_id_seq'),
    server_name      varchar(100) not null unique,
    activate         bool         not null default false,
    hosting_url      varchar(500),
    health_check_url varchar(500) not null,
    management_url   varchar(500) not null,
    user_id          varchar(100),
    created_at       timestamp    not null default current_timestamp,
    modified_at      timestamp    not null default current_timestamp,
    foreign key (user_id) references user_info (username)
);