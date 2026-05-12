FROM maven:3.9.9-eclipse-temurin-17 AS build
LABEL authors="shdwd"

WORKDIR /app

COPY CitaBellaAPI/pom.xml .
RUN mvn dependency:go-offline

COPY CitaBellaAPI/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN apt-get update && apt-get install -y netcat-openbsd curl

COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]