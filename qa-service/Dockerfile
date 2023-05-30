# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /qa

# Copy jar file
COPY qa-service/target/qa-service-0.0.1-SNAPSHOT.jar /qa/qa-service-0.0.1-SNAPSHOT.jar
#COPY target/qa-service-0.0.1-SNAPSHOT.jar /qa/qa-service-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/qa/qa-service-0.0.1-SNAPSHOT.jar"]


