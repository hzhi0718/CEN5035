package test;

import client.CircleClient;
import communication.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class CircleClientTest {

    private CircleClient client;

    @Before
	public void initClient() throws IOException {
		client = new CircleClient("huang zhi", new TestHandler());
	}

    @Test
    public void testTextMessage() throws IOException {
        ArrayList<String> desList = new ArrayList<>();
        desList.add("huang zhi");
        Message message = Message.builder().messageType(Message.TEXT)
                .messageSrcID("huang zhi")
                .messageDesIDList(desList)
                .messageContent("Echo test message")
                .build();
        client.sendTextMessage(message);
    }

}
