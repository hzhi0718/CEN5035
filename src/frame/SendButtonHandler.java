package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import lombok.Builder;
import client.CircleClientMessageSender;

@Builder
public class SendButtonHandler implements ActionListener{
	
	private JTextArea textArea;
	private JTextField msgField;
	private CircleClientMessageSender sender;

	/**
	 * Handle the event after click the button.
	 * */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String text = msgField.getText();
		msgField.setText("");
		textArea.append(text+"\n");
		sender.sendText(text);
	}
}
