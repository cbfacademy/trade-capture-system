# Use official OpenJDK runtime as parent image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Create data directory for H2 database
RUN mkdir -p /app/data

# Run the application
CMD ["java", "-jar", "target/backend-1.0-SNAPSHOT.jar"]
