FROM gradle:jdk21-jammy as build

ENV NEXUS_REPOSITORY_USER=${NEXUS_REPOSITORY_USER}
ENV NEXUS_REPOSITORY_PASSWORD=${NEXUS_REPOSITORY_PASSWORD}
ENV NEXUS_MAVEN_REPOSITORY_URL=${NEXUS_MAVEN_REPOSITORY_URL}

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