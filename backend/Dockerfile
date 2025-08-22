# Use a slim JRE for a smaller, more secure final image.
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the pre-built JAR from the build context into the image.
# IMPORTANT: You must run './gradlew build' in the 'backend' directory first!
COPY build/libs/*.jar app.jar

# Set the active Spring profile for the application
ENV SPRING_PROFILES_ACTIVE=default

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
