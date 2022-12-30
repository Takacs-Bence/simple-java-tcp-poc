package com.takacsbence.simpletcp;

import java.util.Scanner;

public class ClientThread extends Thread {

    private final Client client;
    private final MessageParser messageParser;
    private final String userName;

    public ClientThread(Client client, String userName) {
        this.client = client;
        this.userName = userName;
        messageParser = new TerminalMessageParser();
        start();
    }

    @Override
    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (!message.isEmpty()) {
                Message msg = new Message(userName, message);
                System.out.println(messageParser.prettyPrint(msg));
                client.processMessage(msg);
            }
        }
    }
}
