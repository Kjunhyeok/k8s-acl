FROM openjdk:11-jre-slim-buster
WORKDIR /build
COPY build/libs/*-0.0.1-SNAPSHOT.jar /build/app.jar
ENTRYPOINT ["java","-jar","/build/app.jar"]