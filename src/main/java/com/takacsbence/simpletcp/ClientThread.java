package com.takacsbence.simpletcp;

import java.util.Scanner;

public class ClientThread extends Thread {

    private final Client client;

    public ClientThread(Client client) {
        this.client = client;
        start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println("input message from client: " + input);
        client.processMessage(input);
    }
}
