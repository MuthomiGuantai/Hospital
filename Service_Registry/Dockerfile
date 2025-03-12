FROM openjdk:17-jdk-slim AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/Service_Registry-0.0.1-SNAPSHOT.jar service-registry.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "service-registry.jar"]