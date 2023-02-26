FROM openjdk:11 AS build
WORKDIR src/
COPY build/libs/movie-app-0.1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]