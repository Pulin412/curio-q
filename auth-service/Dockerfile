# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /auth

# Copy jar file
COPY auth-service/pom.xml .
COPY auth-service/src ./src
COPY auth-service/target/auth-service-0.0.1-SNAPSHOT.jar /auth/auth-service-0.0.1-SNAPSHOT.jar

# Download the project dependencies specified in pom.xml
#RUN mvn dependency:go-offline -B

EXPOSE 8080

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://user-db-container:5432/user-db
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=admin
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=none

# Run Maven build
#RUN mvn clean install

# Command to run the application
CMD ["java", "-jar", "/auth/auth-service-0.0.1-SNAPSHOT.jar"]


