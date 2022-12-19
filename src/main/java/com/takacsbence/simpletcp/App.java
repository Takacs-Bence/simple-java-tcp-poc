package com.takacsbence.simpletcp;

public class App {

    /**
     * @throws IllegalArgumentException if the first argument does not match any of the {@link AppMode} values
     */
    public static void main(String[] args) throws Exception {
        AppMode appMode = AppMode.valueOf(args[0]);
        int port = 5000;
        if (appMode == AppMode.SERVER) {
            new Server(port);
        } else if (appMode == AppMode.CLIENT) {
            String host = "0.0.0.0";
            new Client(host, port);
        }
    }

    private enum AppMode {
        SERVER,
        CLIENT
    }
}
