# Base image with Java 17
FROM maven:3.8.4-openjdk-17

# working directory
WORKDIR /sec-lib

# Copy jar file
COPY security-lib/pom.xml .
COPY security-lib/src ./src
COPY security-lib/target/security-lib-0.0.1-SNAPSHOT.jar /sec-lib/security-lib-0.0.1-SNAPSHOT.jar

EXPOSE 9090

# Set environment variables
ENV server.port=9090

# Run Maven build
RUN mvn clean package -Dmaven.test.skip
# OR
# Download the project dependencies specified in pom.xml
#RUN mvn dependency:go-offline -B

# Command to run the application
CMD ["java", "-jar", "/sec-lib/security-lib-0.0.1-SNAPSHOT.jar"]


