package client;

import client.media.AudioReceiver;
import client.media.VideoFrameReceiver;
import communication.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CircleClientMessageReceiver implements Runnable{

	public boolean isOnline;
	private Socket socket;
    private ReceiverHandler receiverHandler;

	public CircleClientMessageReceiver(ReceiverHandler receiverHandler) throws IOException {
		this.socket = CircleClientConfig.getInstance().getSocket();
        this.receiverHandler = receiverHandler;
		isOnline = true;
	}

	public void run() {
		// TODO Auto-generated method stub
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (isOnline) {
                Message message = (Message) objectInputStream.readObject();
                if (message.getMessageType() == Message.VIDEO_INVITATION) {
                    Socket videoSocket = createMediaSocket(message);
                    new Thread(new VideoFrameReceiver(videoSocket)).start();
                } else if (message.getMessageType() == Message.VOICE_INVITATION) {
                    Socket voiceSocket = createMediaSocket(message);
                    new Thread(new AudioReceiver(voiceSocket)).start();
                } else {
                    receiverHandler.reaction(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    private Socket createMediaSocket(Message message) {
        Socket mediaSocket = new Socket();
        InetSocketAddress socketAddress = null;
        if (message.getMessageType() == Message.VIDEO_INVITATION) {
            socketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VIDEO_PORT);
        } else if(message.getMessageType() == Message.VOICE_INVITATION) {
            socketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VOICE_PORT);
        }
        try {
            mediaSocket.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaSocket;
    }

}
