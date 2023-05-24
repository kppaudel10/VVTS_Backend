# Use a base image with Java 11
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/VVTS-0.0.1-SNAPSHOT.jar app.jar

# Expose the desired port
EXPOSE 8848

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
