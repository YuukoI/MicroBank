FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
RUN apk add --no-cache bash
COPY target/*.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--timeout=30", "--strict", "--", "java", "-jar", "/app.jar"]