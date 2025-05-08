# Application Management Service

A Spring Boot application that provides REST APIs for managing application information, following OAuth2 specifications.

## Features

- CRUD operations for application data
- OAuth2 compliant application management
- Swagger UI for API documentation
- Asciidoctor documentation
- PostgreSQL database integration
- Spring Security implementation
- Docker support

## Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- PostgreSQL 12 or higher
- Docker and Docker Compose (for containerized deployment)

## Getting Started

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/yourusername/metadata.git
cd metadata
```

2. Configure PostgreSQL:
   - Create a database named `metadata`
   - Update `application.yml` with your database credentials if needed

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Docker Deployment

1. Build and run using Docker Compose:
```bash
# Build the application
./gradlew build

# Start the containers
docker-compose up -d
```

2. Stop the containers:
```bash
docker-compose down
```

3. View logs:
```bash
docker-compose logs -f
```

## API Documentation

### Swagger UI
Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

### Asciidoctor Documentation
After building the project, you can find the Asciidoctor documentation at:
`build/docs/asciidoc/index.html`

## API Endpoints

### Create Application
- **POST** `/api/v1/applications`
- Creates a new application with the specified details

### Get Application
- **GET** `/api/v1/applications/{appId}`
- Retrieves an application by its ID

## Data Models

### Application Request
| Field | Type | Description | Constraints |
|-------|------|-------------|-------------|
| appId | String | Application ID | Required |
| appName | String | Application name | Required |
| scopes | Set<String> | Application scopes | At least one scope required |
| secretKey | String | Secret key | Required |
| cipherKey | String | Cipher key | Required |
| redirectUri | String | Redirect URI | Optional |
| grantTypes | Set<String> | OAuth2 grant types | At least one grant type required |

### Application Response
| Field | Type | Description |
|-------|------|-------------|
| appId | String | Application ID |
| appName | String | Application name |
| scopes | Set<String> | Application scopes |
| secretKey | String | Secret key |
| cipherKey | String | Cipher key |
| redirectUri | String | Redirect URI |
| grantTypes | Set<String> | OAuth2 grant types |
| createdDate | DateTime | Creation date |
| modifiedDate | DateTime | Last modification date |

## Security

The application uses Spring Security for authentication and authorization. All API endpoints are secured and require proper authentication.

## Testing

Run the tests using:
```bash
./gradlew test
```

## Building

Build the project using:
```bash
./gradlew build
```

## Docker

### Building the Docker Image
```bash
docker build -t metadata-app .
```

### Running with Docker Compose
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f
```

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details. 