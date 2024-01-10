FROM gradle:7.4.1-jdk8 AS build

WORKDIR /app

COPY backend/build.gradle backend/settings.gradle ./

RUN gradle dependencies --no-daemon

COPY backend/ .

RUN gradle bootJar --no-daemon

FROM openjdk:8-jre-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
COPY backend/.env .env

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
