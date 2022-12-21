package com.takacsbence.simpletcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {

    Socket socket;
    DataInputStream din;
    DataOutputStream dout;

    public Client(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        System.out.println("client connected to server on socket: " + socket);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        new ClientThread(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = din.readUTF();
                if (!message.isEmpty()) {
                    System.out.println(socket + " received a message: " + message);
                }
            }
        } catch (EOFException ignore) {/* is not a problem*/} catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void processMessage(String message) {
        try {
            dout.writeUTF(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }
}
