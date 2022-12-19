package com.takacsbence.simpletcp;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    private Server server;
    private Socket socket;

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
                server.sendToAll(message);
            }

        } catch (EOFException ignore) {
        } catch (IOException ie) {
            ie.printStackTrace();
            server.removeConnection(socket);
        }
    }

}
