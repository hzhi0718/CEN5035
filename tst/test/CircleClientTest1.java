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
        desList.add("huang zhi");
        Message message = new Message();
        message.setMessageType(Message.TEXT);
        message.setMessageSrcID("huang zhi");
        message.setMessageDesIDList(desList);
        message.setMessageContent("Hello world.");
        client.sendTextMessage(message);
        client.sendTextMessage(message);
        client.sendTextMessage(message);
    }
}
