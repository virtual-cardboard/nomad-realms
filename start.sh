#!/bin/bash

# Start the server
echo "Starting Server..."
mvn -f nomad-realms-server/pom.xml exec:java -Dexec.mainClass=nomadrealms.ServerMain > server.log 2>&1 &

# Wait for server to initialize
sleep 5

# Start two client instances
echo "Starting Client 1..."
mvn -f nomad-realms/pom.xml exec:java -Dexec.mainClass=nomadrealms.app.NomadRealmsMain > client1.log 2>&1 &

echo "Starting Client 2..."
mvn -f nomad-realms/pom.xml exec:java -Dexec.mainClass=nomadrealms.app.NomadRealmsMain > client2.log 2>&1 &

echo "Server and clients are starting. Check *.log files for output."
