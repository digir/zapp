FROM openjdk:21

WORKDIR /app

COPY project_scaffolding_server/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]