package server;
import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable{

	private Socket socket;
	private DataInputStream din;
	private DataOutputStream dout;
	
	public ClientHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.din = new DataInputStream(this.socket.getInputStream());
		this.dout = new DataOutputStream(this.socket.getOutputStream());
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				// received the message from client
				String recvMsg = din.readUTF();

				// do something here
				System.out.println("The Server received message: " + recvMsg);
				dout.writeUTF("echo: "+recvMsg);
				dout.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				break;
			}
		}
	}

}
