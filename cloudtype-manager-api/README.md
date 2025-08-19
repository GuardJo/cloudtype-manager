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
string username pk
    string password "not null"
string name "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
}

server_info {
bigint id pk "auto increment"
string server_name uk "not null"
bool activate "not null"
string hosting_url
string management_url "not null"
string health_check_url "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
string user_id fk "user_info"
}

refresh_token {
bigint id pk "auto increment"
string token uk "not null"
timestamp createdAt "not null"
timestamp modifiedAt "not null"
string user_id fk "user_info"
}

user_info ||--o{ server_info: "user_id"
user_info o|--|| refresh_token: "user_id"
```