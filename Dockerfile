# Use official Java image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Run jar
CMD ["java", "-jar", "target/*.jar"]
