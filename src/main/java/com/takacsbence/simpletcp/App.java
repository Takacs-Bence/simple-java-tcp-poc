package com.takacsbence.simpletcp;

public class App {

    /**
     * @throws IllegalArgumentException if the first argument does not match any of the {@link AppMode} values
     */
    public static void main(String[] args) {
        try {
            AppMode appMode = AppMode.valueOf(args[0]);
            int port = 5001;
            if (appMode == AppMode.SERVER) {
                new Server(port);
            } else if (appMode == AppMode.CLIENT) {
                String name = args[1];
                String host = "0.0.0.0";
                new Client(host, port, name);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new IllegalStateException();
        }
    }

    private enum AppMode {
        SERVER,
        CLIENT
    }
}
