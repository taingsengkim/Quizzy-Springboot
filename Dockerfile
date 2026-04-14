# Use JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy jar
COPY build/libs/*.jar app.jar

# Expose port (Render uses it)
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]