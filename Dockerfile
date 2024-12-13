       FROM openjdk:23-jdk
       WORKDIR /app
       COPY target/producto1-0.0.1-SNAPSHOT.jar app.jar
       EXPOSE 8080
       ENTRYPOINT ["java", "-jar", "app.jar"]