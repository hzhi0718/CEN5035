package frame;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import client.CircleClientMessageReceiver;
import client.CircleClientMessageSender;

public class CircleClientFrame extends JFrame implements ActionListener {

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
		this.init();
		this.startThreads();
	}
	
	public void goOffline() {
		receiver.isOnline = false;
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
		butSend.addActionListener(this);

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
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Client is going offline");
				goOffline();
				System.exit(0);
			}
		});
	}

	/**
	 * Initialize the sender and receiver.
	 * */
	private void init() {
		try {
			sender = new CircleClientMessageSender();
			receiver = new CircleClientMessageReceiver(textArea);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Start receiving the message.
	 * */
	private void startThreads() {
		recevierThread = new Thread(receiver);
		recevierThread.start();
	}
	
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
