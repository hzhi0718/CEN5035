package frame;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import client.CircleClientMessageReceiver;
import client.CircleClientMessageSender;
import lombok.Builder;

public class CircleClientFrame extends JFrame {

	public static int WIDTH = 500;
	public static int HEIGHT = 400;
	public static String TITLE = "Circle";
	// UI component
	private JScrollPane textPane;
	private JTextArea textArea;
	private JTextField msgField;
	private JButton butSend;
	// the receiver and sender of the client
	private CircleClientMessageReceiver receiver;
	private Thread recevierThread;
	private CircleClientMessageSender sender;

	public void goOnline() {
		this.initFrame();
		this.initThreads();
		this.addListeners();
	}
	
	/**
	 * Create the UI
	 * */
	private void initFrame() {
		setLayout(null);
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textPane = new JScrollPane(textArea);

		butSend = new JButton("Send");
		msgField = new JTextField(255);
		
		textPane.setBounds(5, 5, WIDTH - 25, HEIGHT - 80);
		msgField.setBounds(5, HEIGHT - 70, WIDTH - 220, 24);
		butSend.setBounds(WIDTH - 195, HEIGHT - 70, 80, 24);
		
		add(textPane);
		add(butSend);
		add(msgField);

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void addListeners() {
		this.addSendButtonActionListener();
		this.addCloseWindowListener();
	}
	
	private void addSendButtonActionListener() {
		SendButtonHandler buttonHandler = 
				SendButtonHandler.builder().msgField(msgField)
											.textArea(textArea)
											.sender(sender)
											.build();
		butSend.addActionListener(buttonHandler);
	}
	
	private void addCloseWindowListener() {
		WindowCloseHandler windowCloseHandler = 
				WindowCloseHandler.builder().receiver(receiver).build();
		this.addWindowListener((windowCloseHandler));
	}
	
	/**
	 * Initialize the sender and receiver.
	 * */
	private void initThreads() {
		try {
			sender = new CircleClientMessageSender();
			receiver = new CircleClientMessageReceiver(textArea);
			recevierThread = new Thread(receiver);
			recevierThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
