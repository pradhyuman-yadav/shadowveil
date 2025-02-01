# Use Amazon Linux as the base image
FROM amazonlinux:latest

# Set environment variables to use the system-installed JDK 23
ENV JAVA_HOME=/opt/java-23
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Set the working directory inside the container
WORKDIR /app

# Install necessary dependencies
RUN yum update -y && yum install -y tar gzip

# Verify Java installation (optional)
RUN java -version

# Copy the application JAR file from your host
COPY Jar/videoplatform-0.0.2-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with Java 23
ENTRYPOINT ["java", "-jar", "app.jar"]
