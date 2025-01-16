FROM gradle:jdk21-jammy as build

WORKDIR /app

COPY src ./src
COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle
COPY gradle.properties ./gradle.properties

RUN gradle build --no-daemon -x test --warning-mode all

FROM openjdk:21-jdk-slim

RUN mkdir /app

COPY . .
COPY --from=build /app/build/libs/*.jar ./application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]