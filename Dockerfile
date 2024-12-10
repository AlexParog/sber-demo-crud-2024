FROM bellsoft/liberica-openjdk-debian:17
LABEL authors="alexanderbibik"

WORKDIR /app

COPY target/demo-crud-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080