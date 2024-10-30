FROM openjdk:11-jdk-slim
WORKDIR /app
COPY Solution.java .

Run javac Solution.java

CMD ["java", "Solution"]