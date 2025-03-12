FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Api_Gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
EXPOSE 8100
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]