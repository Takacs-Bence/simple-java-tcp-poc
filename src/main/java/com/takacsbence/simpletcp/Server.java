package com.takacsbence.simpletcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Map<Socket, DataOutputStream> outputStreamsMap = new HashMap<>();

    public Server(int port) throws IOException {
        listen(port);
    }

    public synchronized void sendToAll(String message) throws IOException {
        for (Map.Entry<Socket, DataOutputStream> entry : outputStreamsMap.entrySet()) {
            DataOutputStream dataOutputStream = entry.getValue();
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }
    }

    public synchronized void removeConnection(Socket socket) {
        if (socket != null) {
            outputStreamsMap.remove(socket);
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void listen(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server socket is listening on " + serverSocket);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket);
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            outputStreamsMap.put(socket, dout);
            new ServerThread(this, socket);
        }

    }
}