package com.takacsbence.simpletcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        ServerSocket serverSocket = new ServerSocket(port);
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
}
