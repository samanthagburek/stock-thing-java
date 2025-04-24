# Use a lightweight JDK base image
FROM eclipse-temurin:23-jdk-alpine

# Set work directory
WORKDIR /app

# Copy the built jar into the container
COPY target/stock-thing-java-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
