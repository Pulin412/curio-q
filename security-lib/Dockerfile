# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /sec-lib

# Copy jar file
COPY security-lib/target/security-lib-0.0.1-SNAPSHOT.jar /sec-lib/security-lib-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/sec-lib/security-lib-0.0.1-SNAPSHOT.jar"]


