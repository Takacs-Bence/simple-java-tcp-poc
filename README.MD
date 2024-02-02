# simple TCP chat server

Terminal app with server and client functionality for chatting on localhost.
Secured with SSL.

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

server.port - change the port on which the server is listening.
server.ssl.key-store - path, should be outside of project
server.ssl.key-store-alias - alias to change in following configs
server.ssl.trust-store - trust store path
server.ssl.trust-store-password - trust store password

1. Generate PKCS12 Keystore (with same project JDK)
```
keytool -genkey -keyalg RSA -keystore keystore.p12 -storetype PKCS12 -keysize 2048
```

2. Export Certificate from PKCS12 Keystore: (change keystore-alias)
```
keytool -export -alias ${keystore-alias} -keystore keystore.p12 -file server.crt -storetype PKCS12
```

3. Import Certificate into Truststore (PKCS12): (change keystore-alias)
```
keytool -import -alias ${keystore-alias} -file server.crt -keystore truststore.p12 -storetype PKCS12
```


