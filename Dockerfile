# Build stage
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar blog-zone.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","blog-zone.jar"]