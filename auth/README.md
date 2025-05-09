# Auth Service

OAuth2-compliant token management service built with Spring Boot.

## Features

- OAuth2 token issuance and validation
- JWT token support
- Token revocation
- API documentation with Spring REST Docs and Swagger UI
- Docker support

## Prerequisites

- Java 17 or higher
- Gradle 8.x
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL (for database)

## Getting Started

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/yourusername/auth.git
cd auth
```

2. Configure the database:
   - Create a PostgreSQL database
   - Update `application.yml` with your database credentials

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- REST Docs: `http://localhost:8080/docs/index.html`

### Docker Deployment

1. Build the Docker image:
```bash
docker build -t auth-service .
```

2. Run with Docker Compose:
```bash
docker-compose up -d
```

This will start:
- Auth Service on port 8080
- PostgreSQL on port 8000

## API Endpoints

### Get Access Token
```http
POST /oauth/token
Content-Type: application/json

{
    "loginId": "your-username",
    "password": "your-password"
}
```

### Revoke Token
```http
POST /oauth/revoke
Authorization: Bearer your-token
```

## Configuration

The application can be configured through environment variables or `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:8000/postgres
    username: postgres
    password: 1q2w3e4r
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-jwt-secret
  expiration: 3600
```

## Development

### Running Tests
```bash
./gradlew test
```

### Building Documentation
```bash
./gradlew asciidoctor
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 