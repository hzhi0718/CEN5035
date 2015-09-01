package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class CircleClientMessageSender {
	
	private CircleClientConfig config;
	private Socket socket;
	private DataOutputStream dout;
	
	public CircleClientMessageSender() throws IOException {
		config = CircleClientConfig.getInstance();
		socket = config.getSocket();
		dout = new DataOutputStream(socket.getOutputStream());
	}

	/**
	 * Send text message
	 * */
	public void sendText(String text) {
		try {
			dout.writeUTF(text);
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Text send fail");
			e.printStackTrace();
			
		}
		
	}
}
