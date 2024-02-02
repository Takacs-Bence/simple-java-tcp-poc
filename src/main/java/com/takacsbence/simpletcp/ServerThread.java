package com.takacsbence.simpletcp;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Stack;

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
					if (!handle_cmd(message)) {
						server.sendToAll(socket, message);
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				server.removeConnection(socket);
			}
		}
	/** 	
	 * returns true if the message is a command and will act on it as well
	 */
	private boolean handle_cmd(String message) {
		String[] tokens = message.split("\\|");
		// token: CMD|${command}|${value}  
		if (tokens.length == 3  && tokens[0].equals("CMD")) {	
			if (tokens[1].equalsIgnoreCase("username")) {
				String username = tokens[2];
				if (!server.is_username_reserved(username)) {
					server.add_username(username);
				}
			}
			System.out.println("command handled");
			return true;
		}
		return false;
	}
}
