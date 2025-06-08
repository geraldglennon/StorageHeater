FROM alpine/java:21-jdk

COPY storage-heater-server/target/app.jar /app.jar

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]