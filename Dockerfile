# OpenJDK Base image
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY core-app/build/libs/linkfit-server-app.jar server-app.jar
ENTRYPOINT ["java", "-jar", "server-app.jar", "--spring.profiles.active=dev"]