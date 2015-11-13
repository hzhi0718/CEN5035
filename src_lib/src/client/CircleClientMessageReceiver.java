package client;

import client.media.AudioReceiver;
import client.media.AudioSender;
import client.media.VideoFrameReceiver;
import client.media.VideoFrameSender;
import communication.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CircleClientMessageReceiver implements Runnable{

	public boolean isOnline;
	private Socket socket;
    private ReceiverHandler receiverHandler;
    private CircleClientMessageSender sender;
    private InetSocketAddress socketAddress;

    private VideoFrameSender videoFrameSender;
    private AudioSender audioSender;

	public CircleClientMessageReceiver(ReceiverHandler receiverHandler,
                                       CircleClientMessageSender sender) throws IOException {
		this.socket = CircleClientConfig.getInstance().getSocket();
        this.receiverHandler = receiverHandler;
        this.sender = sender;
		isOnline = true;
	}

	public void run() {
		// TODO Auto-generated method stub
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            JFrame dummy = new JFrame();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            dummy.setLocation(dim.width/2-dummy.getSize().width/2, dim.height/2-dummy.getSize().height/2);
            dummy.setAlwaysOnTop(true);

            while (isOnline) {
                Message message = (Message) objectInputStream.readObject();
                System.out.println("type:"+message.getMessageType());
                if (message.getMessageType() == Message.VIDEO_INVITATION) {
                    String reminderContent =
                            "Your friend " + message.getMessageSrcID() + " would like to start a video chat!";
                    String reminderTitle = "A video chat invitation";
                    dummy.setVisible(false);
                    int dialogResult =
                            JOptionPane.showConfirmDialog(dummy, reminderContent, reminderTitle, JOptionPane.OK_CANCEL_OPTION);
                    if(dialogResult == JOptionPane.OK_OPTION) {
                        socketAddress = createSocketAddress(message);
                        sender.sendMessage(buildAcceptMessage(message, Message.VIDEO_INVITATION_RESPONSE));
                    }
                    dummy.dispose();
                } else if (message.getMessageType() == Message.VOICE_INVITATION) {
                    String reminderContent =
                            "Your friend " + message.getMessageSrcID() + " would like to start a voice chat!";
                    String reminderTitle = "A voice chat invitation";
                    dummy.setVisible(false);
                    int dialogResult =
                            JOptionPane.showConfirmDialog(dummy, reminderContent, reminderTitle, JOptionPane.OK_CANCEL_OPTION);
                    if(dialogResult == JOptionPane.OK_OPTION) {
                        socketAddress = createSocketAddress(message);
                        sender.sendMessage(buildAcceptMessage(message, Message.VOICE_INVITATION_RESPONSE));
                    }
                    dummy.dispose();
                } else if (message.getMessageType() == Message.VIDEO_INVITATION_RESPONSE
                        && message.getMessageContent().equals(Message.ACCEPT)) {
                    // the other side accept the video chat invitation
                    sender.sendMessage(buildServerReadyMessage(message, Message.VIDEO_SERVER_READY));
                    startVideoServer();

                } else if (message.getMessageType() == Message.VOICE_INVITATION_RESPONSE
                        && message.getMessageContent().equals(Message.ACCEPT)) {
                    // the other side accept the voice chat invitation
                    sender.sendMessage(buildServerReadyMessage(message, Message.VOICE_SERVER_READY));
                    startVoiceServer();
                } else if (message.getMessageType() == Message.VIDEO_SERVER_READY) {
                    startVideoClient();
                } else if (message.getMessageType() == Message.VOICE_SERVER_READY) {
                    startVoiceClient();
                } else {
                    receiverHandler.reaction(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    private InetSocketAddress createSocketAddress(Message message) {
        InetSocketAddress socketAddress = null;
        if (message.getMessageType() == Message.VIDEO_INVITATION) {
            socketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VIDEO_PORT);
        } else if(message.getMessageType() == Message.VOICE_INVITATION) {
            socketAddress = new InetSocketAddress(message.getMessageContent(), CircleClientConfig.VOICE_PORT);
        }
        System.out.println(socketAddress.toString());
        System.out.println(socketAddress.getAddress());
        System.out.println(socketAddress.getPort());
        return socketAddress;
    }

    private void startVideoServer() {
        try {
            ServerSocket videoServer = new ServerSocket(CircleClientConfig.VIDEO_PORT);
            videoServer.setSoTimeout(CircleClientConfig.TIME_OUT);
            // start as a sever, and waiting for the other end to response
            System.out.println("Waiting clients...");
            Socket videoSocket = videoServer.accept();
            videoFrameSender = new VideoFrameSender(videoSocket);
            new Thread(videoFrameSender).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startVideoClient() throws IOException, ClassNotFoundException {
        Socket videoSocket = new Socket();
        System.out.println(socketAddress.getAddress()+":"+socketAddress.getPort());
        videoSocket.connect(socketAddress);
        new Thread(new VideoFrameReceiver(videoSocket)).start();
    }

    private void startVoiceServer() {
        try {
            ServerSocket voiceServer = new ServerSocket(CircleClientConfig.VOICE_PORT);
            voiceServer.setSoTimeout(CircleClientConfig.TIME_OUT);
            // start as a sever, and waiting for the other end to response
            Socket voiceSocket = voiceServer.accept();
            audioSender = new AudioSender(voiceSocket);
            new Thread(audioSender).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startVoiceClient() throws IOException {
        Socket voiceSocket = new Socket();
        voiceSocket.connect(socketAddress);
        new Thread(new AudioReceiver(voiceSocket)).start();
    }

    private Message buildAcceptMessage(Message receivedMessage, int type) {
        ArrayList<String> desList = new ArrayList<>();
        desList.add(receivedMessage.getMessageSrcID());
        Message message = new Message();
        message.setMessageType(type);
        message.setMessageContent(Message.ACCEPT);
        message.setMessageDesIDList(desList);
        message.setMessageSrcID(receivedMessage.getMessageDesIDList().get(0));
        return message;
    }

    private Message buildServerReadyMessage(Message receivedMessage, int type) {
        ArrayList<String> desList = new ArrayList<>();
        desList.add(receivedMessage.getMessageSrcID());
        Message message = new Message();
        message.setMessageType(type);
        message.setMessageDesIDList(desList);
        message.setMessageSrcID(receivedMessage.getMessageDesIDList().get(0));
        return message;
    }
}
