package com.takacsbence.simpletcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = din.readUTF();
                System.out.println(socket + " received a message: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void processMessage(String message) {
        try {
            dout.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
