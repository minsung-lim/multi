FROM gradle:8.12-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :apigw:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/apigw/build/libs/apigw.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 