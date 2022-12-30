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

    private final MessageParser messageParser;

    public Client(String host, int port, String userName) throws IOException {
        messageParser = new TerminalMessageParser();
        try {
            socket = new Socket(host, port);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        System.out.println("client connected to server on socket: " + socket);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        new ClientThread(this, userName);
        new Thread(this).start();
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
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void processMessage(Message message) {
        try {
            dout.writeUTF(messageParser.parseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }
}
