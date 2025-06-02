FROM eclipse-temurin:21-jre-alpine-3.21

RUN mkdir /opt/app

COPY target/example-websocket.war /opt/app

CMD [ "java", "-jar", "/opt/app/example-websocket.war" ]