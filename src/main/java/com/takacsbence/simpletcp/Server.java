package com.takacsbence.simpletcp;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.*;

public class Server {

    private final Map<Socket, DataOutputStream> outputStreamsMap = new HashMap<>();
	private final Set<String> users = new HashSet<>();

    public Server(int port) throws IOException {
        listen(port);
    }

	public boolean is_username_reserved(String username) {
		return users.contains(username);
	}

	public synchronized void add_username(String username) {
		users.add(username);
		System.out.println("user joined the server" + username);
	}

    public synchronized void sendToAll(Socket socket, String message) throws IOException {
        for (Map.Entry<Socket, DataOutputStream> entry : outputStreamsMap.entrySet()) {
            int port = entry.getKey().getPort();
            // do not send back to original source
            if (port != socket.getPort()) {
                DataOutputStream dataOutputStream = entry.getValue();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            }
        }
    }

    public synchronized void removeConnection(Socket socket) {
        try {

            if (socket != null) {
                synchronized (outputStreamsMap) {
                    outputStreamsMap.remove(socket);
                }
                socket.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void listen(int port) throws IOException {
        SSLServerSocketFactory sslServerSocketFactory;
        try {
            sslServerSocketFactory = createSSLServerSocketFactory();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
        final ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(port);

        System.out.println("Server socket is listening on " + serverSocket);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket);
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            synchronized (outputStreamsMap) {
                outputStreamsMap.put(socket, dout);
            }

            new ServerThread(this, socket);
        }

    }

    private SSLServerSocketFactory createSSLServerSocketFactory() throws Exception {
        final String keyStorePath = AppConfig.getProperty("server.ssl.key-store");
        final String keyStorePassword = AppConfig.getProperty("server.ssl.key-store-password");
        final char[] password = keyStorePassword.toCharArray();

        KeyStore keyStore = KeyStore.getInstance("PKCS12", "SunJSSE");
        keyStore.load(new FileInputStream(keyStorePath), password);

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
        keyManagerFactory.init(keyStore, password);

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        trustManagerFactory.init(keyStore);

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext.getServerSocketFactory();
    }
}
