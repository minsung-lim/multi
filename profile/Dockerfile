FROM gradle:8.12-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :profile:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/profile/build/libs/profile.jar ./app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"] 