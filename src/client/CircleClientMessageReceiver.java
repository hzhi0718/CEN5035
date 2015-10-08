package client;

import communication.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JTextArea;

public class CircleClientMessageReceiver implements Runnable{

	public boolean isOnline;
	
	private Socket socket;
	private ObjectInputStream objectInputStream;
    private ReceiverHandler receiverHandler;

	public CircleClientMessageReceiver(ReceiverHandler receiverHandler) throws IOException {
		this.socket = CircleClientConfig.getInstance().getSocket();
        this.receiverHandler = receiverHandler;
		isOnline = true;
	}
	
	public void run() {
		// TODO Auto-generated method stub
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (isOnline) {
                Message message = (Message) objectInputStream.readObject();
                receiverHandler.reaction(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}
