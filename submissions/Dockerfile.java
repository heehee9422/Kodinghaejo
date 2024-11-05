FROM openjdk:11-jdk-slim

WORKDIR /app

COPY Verify.java .
COPY Solution.java .
COPY Main.java .

RUN javac Main.java

CMD ["java", "Main"]