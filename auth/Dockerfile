FROM gradle:8.12-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :auth:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/auth/build/libs/auth.jar ./app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"] 