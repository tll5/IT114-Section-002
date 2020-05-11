import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatIntegration {
	static SocketClient client;
	static JTextArea txtArea;
	public static void main(String[] args) {

		JFrame frame = new JFrame("Reversi Chat Integration"); //'Simple Chat Mockup'
		frame.setLayout(new BorderLayout());

		JPanel simpleChat = new JPanel();
		simpleChat.setPreferredSize(new Dimension(400,400));
		simpleChat.setLayout(new BorderLayout());

		JTextArea textArea = new JTextArea();

		textArea.setEditable(false);
		textArea.setText("");
		txtArea = textArea;

		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		chatArea.add(textArea, BorderLayout.CENTER);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));

		simpleChat.add(chatArea, BorderLayout.CENTER);

		JPanel userInput = new JPanel();

		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100,30));

		JButton b = new JButton();
		b.setPreferredSize(new Dimension(100,30));
		b.setText("Send");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = textField.getText();
				if(message.length() > 0) {
					client.sendMessage(message);
				}
			}
			
		});
		//add textfield and button to panel
		userInput.add(textField);
		userInput.add(b);
		simpleChat.add(userInput, BorderLayout.SOUTH);
		//add simpleChat panel to frame
		frame.add(simpleChat, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
}
