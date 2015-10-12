package communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class Message implements Serializable{
	static public int TEXT = 1;
	static public int INVITATION = 2;
	static public int HANDSHAKE = 3;
	
	private int messageType;
	private ArrayList<String> messageDesIDList;
	private String messageSrcID;
	private String messageContent;
	private String messageTimeStamp;

    public Message() {}

    public void setMessageType(int messageType) { this.messageType = messageType; }
    public void setMessageSrcID(String id) { this.messageSrcID = id; }
    public void setMessageDesIDList(ArrayList<String> desIDList) {this.messageDesIDList = desIDList; };
    public void setMessageTimeStamp(String timeStamp) {this.messageTimeStamp = timeStamp; }
    public void setMessageContent(String content) {this.messageContent = content; }

    public int getMessageType() { return this.messageType; }
    public String getMessageSrcID() { return this.messageSrcID; }
    public ArrayList<String> getMessageDesIDList() { return this.messageDesIDList; }
    public String getMessageContent() { return this.messageContent; }
    public String getMessageTimeStamp() { return this.messageTimeStamp; }
}
