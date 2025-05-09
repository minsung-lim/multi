# COZ Microservices

마이크로서비스 아키텍처 기반의 COZ 프로젝트입니다.

## 서비스 구성

| 서비스명 | 포트 | 설명 |
|---------|------|------|
| API Gateway | 8080 | 모든 서비스의 진입점, 라우팅 및 인증 처리 |
| Auth | 8082 | 사용자 인증 및 토큰 관리 |
| Metadata | 8083 | 메타데이터 관리 |
| Profile | 8084 | 사용자 프로필 관리 |

## 기술 스택

- Java 17
- Spring Boot 3.2.3
- Spring Cloud Gateway
- Spring Security
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Gradle

## 실행 방법

### 1. 로컬 개발 환경

1. PostgreSQL 데이터베이스 실행
```bash
docker run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=1q2w3e4r \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=postgres \
  -p 8000:5432 \
  postgres:15
```

2. 프로젝트 빌드
```bash
./gradlew build
```

3. 각 서비스 실행
```bash
# API Gateway
java -jar apigw/build/libs/apigw.jar

# Auth Service
java -jar auth/build/libs/auth.jar

# Metadata Service
java -jar metadata/build/libs/metadata.jar

# Profile Service
java -jar profile/build/libs/profile.jar
```

### 2. Docker Compose

모든 서비스를 한 번에 실행:
```bash
docker-compose up --build
```

## API 문서

각 서비스의 Swagger UI를 통해 API 문서를 확인할 수 있습니다:

- API Gateway: http://localhost:8080/swagger-ui.html
- Auth Service: http://localhost:8082/swagger-ui.html
- Metadata Service: http://localhost:8083/swagger-ui.html
- Profile Service: http://localhost:8084/swagger-ui.html

## 프로젝트 구조

```
coz/
├── apigw/          # API Gateway 서비스
├── auth/           # 인증 서비스
├── metadata/       # 메타데이터 서비스
├── profile/        # 프로필 서비스
├── build.gradle    # 루트 프로젝트 빌드 설정
├── settings.gradle # 프로젝트 설정
└── docker-compose.yml # Docker Compose 설정
```

## 환경 변수

각 서비스의 주요 환경 변수:

- `SPRING_DATASOURCE_URL`: 데이터베이스 URL
- `SPRING_DATASOURCE_USERNAME`: 데이터베이스 사용자명
- `SPRING_DATASOURCE_PASSWORD`: 데이터베이스 비밀번호
- `JWT_SECRET`: JWT 토큰 생성에 사용되는 시크릿 키
- `JWT_EXPIRATION`: JWT 토큰 만료 시간(초)

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 