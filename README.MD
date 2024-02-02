# simple TCP chat server

Terminal app with server and client functionality for chatting on localhost.

## How to build and run

Have maven installed.

Build the jar. mvn clean install

Run the server first from the target dir.

java -jar {jar_with_version}.jar SERVER

Then run any number of clients 

java -jar {jar_with_version}.jar CLIENT {CLIENT_NAME}

Write in the terminal your messages, and you will see other messages incoming from other users.

## Config

Configs are available under src/main/resources/config/config.properties

port: server port


