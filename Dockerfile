# 1. Build Stage: Use a full JDK with Maven. This is the style from the sample project.
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Copy Maven wrapper and project files
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml .

# Pre-download dependencies (cache friendly)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the JAR
RUN ./mvnw package -DskipTests

# 2. Runtime Stage: Use a lightweight JRE
FROM eclipse-temurin:21-jre-jammy
USER root

# Install libpcap and other dependencies
RUN apt-get update && apt-get install -y libpcap-dev
# Ensure the library path is set correctly for JNA
ENV JNA_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu
# Set working directory and create a non-root user
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run the app, entry points consistent with old Dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
