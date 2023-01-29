package com.takacsbence.simpletcp;

import java.io.DataInputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private final Server server;
    private final Socket socket;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        start();
    }

    @Override
    public void run() {
        try {
            DataInputStream din = new DataInputStream(socket.getInputStream());

            while (true) {
                String message = din.readUTF();
                System.out.println("Sending message with content: " + message);
                server.sendToAll(socket, message);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            server.removeConnection(socket);
        }
    }
}
