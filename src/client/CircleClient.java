package client;

import communication.Message;
import lombok.Builder;
import lombok.Setter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhi huang on 2015/10/2.
 * Interact with UI.
 */
@Setter
public class CircleClient {

    private CircleClientMessageSender messageSender;
    private CircleClientMessageReceiver messageReceiver;
    private CircleClientConfig config;
    private String CircleClientID;

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
        Message handShakeMessage = Message.builder()
                .messageType(Message.HANDSHAKE)
                .messageSrcID(CircleClientID)
                .build();
        messageSender.sendMessage(handShakeMessage);
    }

    public void sendTextMessage(Message message) throws IOException {
        message.setMessageTimeStamp(new Date().toString());
        messageSender.sendMessage(message);
    }
}
