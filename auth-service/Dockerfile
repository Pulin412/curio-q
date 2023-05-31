# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /auth

# Copy jar file
COPY ./auth-service/target/auth-service-0.0.1-SNAPSHOT.jar /auth/auth-service-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/auth/auth-service-0.0.1-SNAPSHOT.jar"]
