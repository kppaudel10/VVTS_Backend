# Use a base image with Maven to build the application
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean install -DskipTests

# Use a base image with Java 11 to run the application
FROM openjdk:11
# Copy the built JAR file from the build stage
#COPY --from=build /app/target/VVTS-0.0.1-SNAPSHOT.jar VVTS-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY target/VVTS-0.0.1-SNAPSHOT.jar app.jar


# Expose the desired port
EXPOSE 8848

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
