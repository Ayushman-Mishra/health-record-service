# ==========================================
# Stage 1: Build the JAR with Amazon Corretto 25 & Maven Wrapper
# ==========================================
FROM amazoncorretto:25 AS builder
WORKDIR /app

# Install tar and gzip so mvnw can extract the downloaded Maven distribution
RUN dnf install -y tar gzip && dnf clean all

# Copy Maven Wrapper and POM first for Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Convert Windows CRLF line endings to Linux LF and set execute permission
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download dependencies offline
RUN ./mvnw dependency:go-offline -B

# Copy application source code and package the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Minimal Production Runtime
# ==========================================
FROM amazoncorretto:25-alpine
WORKDIR /app

# Run as a dedicated non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the generated JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Configure production container-aware JVM memory options
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]