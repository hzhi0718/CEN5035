package frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import client.CircleClientMessageReceiver;
import lombok.Builder;

@Deprecated
@Builder
public class WindowCloseHandler extends WindowAdapter {
	private CircleClientMessageReceiver receiver;
	
	public void windowClosing(WindowEvent e) {
		System.out.println("Client is going offline");
		goOffline();
		System.exit(0);
	}
	
	private void goOffline() {
		receiver.isOnline = false;
	}
}
