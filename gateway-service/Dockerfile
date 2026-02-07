FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
RUN apk add --no-cache bash
COPY target/*.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["/wait-for-it.sh", "kafka:9092", "--timeout=60", "--strict", "--", "java", "-jar", "/app.jar"]