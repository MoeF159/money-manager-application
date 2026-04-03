FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/money-manager-0.0.1-SNAPSHOT.jar moneymanger-v1.0.jar
RUN chown -R 10001:0 /app
USER 10001
EXPOSE 9090
ENTRYPOINT [ "java", "-jar", "moneymanger-v1.0.jar" ]