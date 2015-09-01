package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

public class CircleClientMessageReceiver implements Runnable{

	public boolean isOnline;
	
	private Socket socket;
	private JTextArea textArea;
	private DataInputStream din;
	
	public CircleClientMessageReceiver(JTextArea textArea) throws IOException {
		this.socket = CircleClientConfig.getInstance().getSocket();
		this.din = new DataInputStream(socket.getInputStream());
		this.textArea = textArea;
		isOnline = true;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while (isOnline) {
			try {
				textArea.append(din.readUTF()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
