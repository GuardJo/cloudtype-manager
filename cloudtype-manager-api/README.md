# Cloudtype Manager API 모듈

---

# 환경 구성

## 실행환경

- `jdk 17`
- `gradle`
- `spring-boot`

## 환경 변수

```json
{
  "DATABASE_URL": "DB JDBC URL",
  "DATABASE_USERNAME": "DB username",
  "DATABASE_PASSWORD": "DB password",
  "ALLOWED_ORIGIN_SERVER_URL": "UI 모듈 URL"
}
```

## ERD

```mermaid
---
title: Cloudtype Manager ERD
---
erDiagram
    user_info {
varchar(100) username pk
varchar(100) password "not null"
varchar(10) name "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
}

server_info {
bigint id pk "auto increment"
varchar(100) server_name uk "not null"
bool activate "not null"
varchar(500) hosting_url
varchar(500) management_url "not null"
varchar(500) health_check_url "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
varchar(100) user_id fk "user_info"
}

refresh_token {
bigint id pk "auto increment"
varchar(512) token uk "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
varchar(100) user_id fk "user_info"
}

app_push_token {
bigint id pk "auto increment"
varchar(300) token uk "not null"
varchar(300) device "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
varchar(100) user_id fk "user_info"
}

app_push_msg {
bigint id pk "auto increment"
varchar(100) title "not null"
varchar(500) body "not null"
bool push_sent "not null default false"
bool push_received "not null default false"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
bigint token_id fk "not null"
 }

user_info ||--o{ server_info: "user_id"
user_info o|--|{ refresh_token: "user_id"
user_info ||--o{ app_push_token: "user_id"

app_push_token ||--o{ app_push_msg: "token_id"
```