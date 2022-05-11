FROM gradle:7-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11.0.14-jre-slim-buster

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/kafka-twitter-consumer-app.jar

WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "kafka-twitter-consumer-app.jar"]