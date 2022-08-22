FROM maven:3.8.6-jdk-8-slim AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:8-jre

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -Dthorntail.logging=FINE"
ENV APP_DIR = /usr/app
WORKDIR $APP_DIR

COPY --from=build /usr/src/app/target/guacamole-web-docker-0.0.1-thorntail.jar guacamole-web-docker-0.0.1-thorntail.jar

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar guacamole-web-docker-0.0.1-thorntail.jar
