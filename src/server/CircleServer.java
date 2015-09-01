package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CircleServer {
	
	ServerSocket serverSocket;
	
	public CircleServer() throws Exception {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(9999);
	}
	
	public void startService() throws IOException {
		System.out.println("The server is online...");
		while (true) {
			// create a new socket for coming client
			Socket clientSocket = serverSocket.accept();
			// start a new thread to interact with client
			new Thread(new ClientHandler(clientSocket)).start();
		}
	}
}
