# API Gateway

Spring Cloud Gateway 기반의 API Gateway 서비스입니다.

## 기능

- `/profile/{userid}` 요청을 `http://profile.internal`로 라우팅
- `/auth/{code}` 요청을 `http://auth.internal`로 라우팅

## 실행 방법

1. 프로젝트 빌드:
```bash
./gradlew build
```

2. 애플리케이션 실행:
```bash
./gradlew bootRun
```

## 포트

기본 포트: 8080

## 엔드포인트

- Profile 서비스: `http://localhost:8080/profile/{userid}`
- Auth 서비스: `http://localhost:8080/auth/{code}`
- Actuator 엔드포인트: `http://localhost:8080/actuator` 