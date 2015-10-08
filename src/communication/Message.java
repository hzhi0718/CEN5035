package communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class Message implements Serializable{
	static public int TEXT = 1;
	static public int INVITATION = 2;
	static public int HANDSHAKE = 3;
	
	private int messageType;
	private ArrayList<String> messageDesIDList;
	private String messageSrcID;
	private String messageContent;
	private String messageTimeStamp;
}
