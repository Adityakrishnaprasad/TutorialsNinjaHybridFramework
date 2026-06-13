@echo off

echo =====================================
echo Starting Selenium Grid...
echo =====================================
docker compose up -d

echo Waiting for Grid to be ready...
timeout /t 5

echo =====================================
echo Running TestNG Suite...
echo =====================================
mvn clean test -Dsurefire.suiteXmlFiles=grid-parallel-suite.xml
echo =====================================
echo Stopping Selenium Grid...
echo =====================================
docker compose down

echo =====================================
echo Execution Finished
echo =====================================
pause