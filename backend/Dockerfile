FROM amazoncorretto:17-alpine-jdk
ENV SPRING_PROFILE=${SPRING_PROFILE}

ARG JAR_FILE=./backend/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILE}","-jar","/app.jar"]
