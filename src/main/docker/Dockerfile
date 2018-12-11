FROM 10.10.150.39/sdm/openjdk:8u181-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD esl-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-server","-Xms512m","-Xmx512m","-Djava.security.egd=file:/dev/./urandom -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n","-jar","/app.jar"]