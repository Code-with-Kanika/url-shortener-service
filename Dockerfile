# Use Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "target/url-shortener-0.0.1-SNAPSHOT.jar"]
