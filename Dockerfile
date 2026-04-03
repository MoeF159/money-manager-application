# ----------------------------
# Stage 1: Build the JAR
# ----------------------------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy all project files
COPY . .

# Build the jar using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# ----------------------------
# Stage 2: Run the JAR
# ----------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/money-manager-0.0.1-SNAPSHOT.jar moneymanger-v1.0.jar

# Make app folder writable and switch to non-root user
RUN chown -R 10001:0 /app
USER 10001

# Expose app port
EXPOSE 9090

# Run the jar
ENTRYPOINT ["java", "-jar", "moneymanger-v1.0.jar"]