FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Medical_Service-0.0.1-SNAPSHOT.jar medical-service.jar
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "medical-service.jar"]