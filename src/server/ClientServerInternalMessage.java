package server;

import java.net.Socket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ClientServerInternalMessage {
	
	@Setter @Getter @Builder
	static class Connect {
		private Socket socket;
		private MessageRouter messageRouter;
        private boolean isOnline;
	}

}
