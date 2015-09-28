package server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CircleServer extends UntypedActor{
	
	private ServerSocket serverSocket;
	private MessageRouter messageRouter;
	private ArrayList<ActorRef> actorList;

	public CircleServer() throws Exception {
		// TODO Auto-generated constructor stub
		serverSocket = new ServerSocket(9999);
		messageRouter = new MessageRouter();
		actorList = new ArrayList<>();
	}
	
	public void startService() throws IOException {
		System.out.println("The server is online...");
		while (true) {
			// create a new socket for coming client
			Socket clientSocket = serverSocket.accept();
			// start a new actor to handle the client request
			ActorSystem system = ActorSystem.create("CircleServerWorkers");
			ActorRef worker =
					system.actorOf(Props.create(CircleServerWorker.class), "worker");
			// initialization of the work
			ClientServerInternalMessage.Connect connectMessage =
					ClientServerInternalMessage.Connect.builder()
							.socket(clientSocket)
							.messageRouter(this.messageRouter)
							.isOnline(false)
							.build();
			worker.tell(connectMessage, getSelf());
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {

	}
}

