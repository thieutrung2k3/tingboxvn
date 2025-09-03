FROM amazoncorretto:21

WORKDIR /app

COPY target/auth-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]