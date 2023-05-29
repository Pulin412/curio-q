# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /user

# Copy jar file
COPY user-service/target/user-service-0.0.1-SNAPSHOT.jar /user/user-service-0.0.1-SNAPSHOT.jar
COPY user-service/.m2 /root/.m2

# Command to run the application
CMD ["java", "-jar", "/user/user-service-0.0.1-SNAPSHOT.jar"]


