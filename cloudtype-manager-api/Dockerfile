# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -g 10001 -S spring \
    && adduser -u 10001 -S spring -G spring \
    && mkdir -p /app/logs/archived \
    && chown -R spring:spring /app
COPY --from=builder --chown=spring:spring \
    /workspace/build/libs/*.jar /app/app.jar

USER 10001:10001
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
