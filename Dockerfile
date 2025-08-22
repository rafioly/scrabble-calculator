# This Dockerfile assumes the application JAR has already been built locally
# by running './gradlew bootJar' from the project root.

# Stage 1: Use a lightweight Java runtime
FROM openjdk:21-jdk-slim
WORKDIR /app

# Stage 2: Copy the pre-built JAR from the build context into the image
# The build context is the project root, as defined in docker-compose.yml
COPY build/libs/*.jar app.jar

# Set the active Spring profile for the application
ENV SPRING_PROFILES_ACTIVE=caching

# Stage 3: Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
