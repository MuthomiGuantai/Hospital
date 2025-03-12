FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Department_Service-0.0.1-SNAPSHOT.jar department-service.jar
EXPOSE 8103
ENTRYPOINT ["java", "-jar", "department-service.jar"]