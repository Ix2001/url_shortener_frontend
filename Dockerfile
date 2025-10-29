FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

VOLUME /tmp

COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
