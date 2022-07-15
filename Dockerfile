FROM openjdk:8-jdk-alpine

WORKDIR /opt/app

COPY target/iyzico-challenge-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","iyzico-challenge-0.0.1-SNAPSHOT.jar"]