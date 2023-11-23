FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} /app.jar

EXPOSE 9002

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]