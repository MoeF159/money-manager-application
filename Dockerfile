# Stage 1: Build the JAR
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .

# Fix permission for mvnw
RUN chmod +x mvnw

# Build the jar
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/money-manager-0.0.1-SNAPSHOT.jar moneymanger-v1.0.jar

RUN chown -R 10001:0 /app
USER 10001

EXPOSE 9090
ENTRYPOINT ["java","-jar","moneymanger-v1.0.jar"]
CMD ["--spring.profiles.active=prod"]