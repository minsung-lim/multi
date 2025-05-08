FROM gradle:8.12-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :metadata:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/metadata/build/libs/metadata.jar ./app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"] 