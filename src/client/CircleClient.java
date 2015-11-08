package client;

import client.media.AudioSender;
import client.media.VideoFrameSender;
import communication.Message;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by zhi huang on 2015/10/2.
 * Interact with UI.
 */
@Setter
public class CircleClient {

    public CircleClientMessageSender messageSender;
    private CircleClientMessageReceiver messageReceiver;
    private CircleClientConfig config;
    private String CircleClientID;
    private VideoFrameSender videoFrameSender;
    private AudioSender audioSender;

    public CircleClient(String ID, ReceiverHandler receiverHandler, String ip, int port) throws IOException{
        // connect the server
        CircleClientConfig.IP_ADDRESS = ip;
        CircleClientConfig.PORT = port;
        config = CircleClientConfig.getInstance();
        messageSender = new CircleClientMessageSender();
        messageReceiver = new CircleClientMessageReceiver(receiverHandler);
        new Thread(messageReceiver).start();
        this.CircleClientID = ID;
        // to notify the server
        this.sendHandShake();
    }

    /**
     * Use default ip and port. (localhost:9999)
     * */
    public CircleClient(String ID, ReceiverHandler receiverHandler) throws IOException {
        // connect the server
        config = CircleClientConfig.getInstance();
        messageSender = new CircleClientMessageSender();
        messageReceiver = new CircleClientMessageReceiver(receiverHandler);
        new Thread(messageReceiver).start();
        this.CircleClientID = ID;
        // to notify the server
        this.sendHandShake();
    }

    private void sendHandShake() throws IOException {
        Message handShakeMessage = new Message();
        handShakeMessage.setMessageType(Message.HANDSHAKE);
        handShakeMessage.setMessageSrcID(CircleClientID);
        messageSender.sendMessage(handShakeMessage);
    }

    public void sendTextMessage(Message message) throws IOException {
        message.setMessageTimeStamp(new Date().toString());
        messageSender.sendMessage(message);
    }


    public void startVideoChat(Message message) throws IOException{
        ServerSocket videoServer = new ServerSocket(CircleClientConfig.VIDEO_PORT);
        message.setMessageTimeStamp(new Date().toString());
        messageSender.sendMessage(message);
        // time out
        videoServer.setSoTimeout(CircleClientConfig.TIME_OUT);
        // start as a sever, and waiting for the other end to response
        Socket videoSocket = videoServer.accept();
        videoFrameSender = new VideoFrameSender(videoSocket);
        new Thread(videoFrameSender).start();
    }

    public void startVoiceChat(Message message) throws IOException {
        ServerSocket voiceServer = new ServerSocket(CircleClientConfig.VOICE_PORT);
        message.setMessageTimeStamp(new Date().toString());
        messageSender.sendMessage(message);
        // invitation time out
        voiceServer.setSoTimeout(CircleClientConfig.TIME_OUT);
        // start as a sever, and waiting for the other end to response
        Socket voiceSocket = voiceServer.accept();
        audioSender = new AudioSender(voiceSocket);
        new Thread(audioSender).start();
    }
}

