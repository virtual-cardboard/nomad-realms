@echo off

:: Start the server
echo Starting Server...
start /b cmd /c "mvn -f nomad-realms-server/pom.xml exec:java -Dexec.mainClass=nomadrealms.ServerMain > server.log 2>&1"

:: Start two client instances
echo Starting Client 1...
start /b cmd /c "mvn -f nomad-realms/pom.xml exec:java -Dexec.mainClass=nomadrealms.app.NomadRealmsMain > client1.log 2>&1"

echo Starting Client 2...
start /b cmd /c "mvn -f nomad-realms/pom.xml exec:java -Dexec.mainClass=nomadrealms.app.NomadRealmsMain > client2.log 2>&1"

echo Server and clients are starting. Check *.log files for output.
