FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S ecoorbit && adduser -S ecoorbit -G ecoorbit
USER ecoorbit

COPY --from=build /build/target/ecoorbit-api.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/api/status || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
