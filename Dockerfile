FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

# Needs a running Selenium Grid. Pass its address with -e SELENIUM_HUB_URL=http://<grid-host>:4444
CMD ["mvn", "test", "-Dsurefire.suiteXmlFiles=grid-parallel-suite.xml", "-Dexecution_env=remote"]
