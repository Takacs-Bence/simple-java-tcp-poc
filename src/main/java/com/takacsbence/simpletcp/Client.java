package com.takacsbence.simpletcp;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;

public class Client implements Runnable {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    private final MessageParser messageParser;

    public Client(String host, int port, String userName) throws IOException {
		messageParser = new TerminalMessageParser();
		boolean created = true;

		try {
            createSSLSocket(host, port);
        } catch (Exception e) {
            System.err.println("client with username: " + userName + " won't start due to error. " + e.getMessage());
            created = false;
        }

        if (created) {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            new ClientThread(this, userName);
            new Thread(this).start();

            validate_username(userName);

            System.out.printf("client connected to server on socket: %s with username: %s%n", socket, userName);
        }
    }

    private void createSSLSocket(String host, int port) throws Exception {
        String trustStorePath = AppConfig.getProperty("server.ssl.trust-store");
        String trustStorePassword = AppConfig.getProperty("server.ssl.trust-store-password");

        final char[] password = trustStorePassword.toCharArray();

        // Load truststore with the CA's public key
        KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(new FileInputStream(trustStorePath), password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(truststore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        socket = sslSocketFactory.createSocket(host, port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = din.readUTF();
                if (!message.isEmpty()) {
                    Message msg = messageParser.parseMessage(message);
                    System.out.println(messageParser.prettyPrint(msg));
                }
            }
        } catch (EOFException ignore) {/* is not a problem*/} catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void processMessage(Message message) {
        try {
            dout.writeUTF(messageParser.parseMessage(message));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

	private void validate_username(String userName) {
		try {
			dout.writeUTF("CMD|username|" + userName);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

}
