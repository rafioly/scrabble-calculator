# --- Stage 1: Build the Backend Application ---
FROM gradle:jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

# --- Stage 2: Create the final image ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=default
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
