package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * A Singleton class
 * */
public class CircleClientConfig {
	
	// instance
	static CircleClientConfig config = null;
	
	private String IP_ADDRESS = "127.0.0.1";
	private int PORT = 9999;
	private InetSocketAddress SOCKET_ADDRESS = new InetSocketAddress(IP_ADDRESS , PORT);
	private Socket socket; 
	
	static CircleClientConfig getInstance() {
		if (config == null) {
			config = new CircleClientConfig();
		}
		return config;
	}
	
	public CircleClientConfig() {
		socket = new Socket();
		try {
			socket.connect(SOCKET_ADDRESS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
