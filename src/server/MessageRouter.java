package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import communication.Message;
import lombok.NonNull;

public class MessageRouter {
	private HashMap<String, Socket> routingMap = new HashMap<String, Socket>();
	
	public void insertEntry(@NonNull String id, @NonNull Socket socket) {
		routingMap.put(id, socket);
	}
	
	public void removeEntry(@NonNull String id) {
		routingMap.remove(id);
	}
	
	public void forwardMessage(@NonNull Message msg) throws IOException {
        ObjectOutputStream objectOutputStream;
		for (String DesID : msg.getMessageDesIDList()) {
			objectOutputStream = new ObjectOutputStream(routingMap.get(DesID).getOutputStream());
            objectOutputStream.writeObject(msg);
            objectOutputStream.flush();
		}
	}
}
