FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn", "test","grid-parallel-suite.xml]