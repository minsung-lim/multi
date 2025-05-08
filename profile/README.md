# Profile User Service

Spring Boot 기반의 사용자 프로필 관리 REST API 서비스입니다.

## 주요 기능
- 사용자 생성, 조회, 수정, 삭제 (CRUD)
- 요청/응답 및 헤더 로깅
- PostgreSQL + HikariCP 연동
- Docker/Docker Compose 지원
- Swagger UI, Asciidoc API 문서 제공

## API 엔드포인트

| 메서드 | 경로                      | 설명           |
|--------|---------------------------|----------------|
| GET    | `/profile/user`           | 전체 사용자 조회 |
| GET    | `/profile/user/{userId}`  | 특정 사용자 조회 |
| POST   | `/profile/user`           | 사용자 생성     |
| PUT    | `/profile/user/{userId}`  | 사용자 수정     |
| DELETE | `/profile/user/{userId}`  | 사용자 삭제     |

## 실행 방법

### 1. 로컬 개발 환경
```bash
./gradlew build
java -jar build/libs/app.jar
```

### 2. Docker Compose
```bash
docker-compose up --build
```

## API 문서

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Asciidoc (Spring REST Docs)**: 빌드 후 `build/docs/asciidoc/index.html`에서 확인
