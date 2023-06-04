# Use a base image with Java and Maven pre-installed
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the project source code
COPY src ./src

# Build the project
RUN mvn clean install

# Create a new stage for running the application
FROM openjdk:17-slim-buster


# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/bookqualifier-0.0.1-SNAPSHOT.jar .

# Expose the port on which the application will listen
EXPOSE 8080

# Set the command to run the application when the container starts
CMD ["java", "-Dspring.profiles.active=production", "-jar", "bookqualifier-0.0.1-SNAPSHOT.jar"]
