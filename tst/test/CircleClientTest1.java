package test;

import client.CircleClient;
import communication.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class CircleClientTest1 {

    private CircleClient client;

    @Before
	public void initClient() throws IOException {
		client = new CircleClient("huang zhi", new TestHandler(),"localhost", 9999);
	}

    @Test
    public void testTextMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.TEXT);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("Hello world.");
        client.sendTextMessage(message);
        client.sendTextMessage(message);
        client.sendTextMessage(message);
    }

    @Test
    public void testVideoMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.VIDEO_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("127.0.0.1");
        client.startVideoChat(message);
        while (true) {

        }
    }

    @Test
    public void testVoiceMessage() throws IOException {

        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi2");
        Message message = new Message();
        message.setMessageType(Message.VOICE_INVITATION);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("127.0.0.1");

        client.startVoiceChat(message);
        while (true) {

        }
    }
}
