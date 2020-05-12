import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ReversiClient extends JFrame implements OnReceive {
	public static Point testPoint = new Point(0,0);
	public static HashMap<Point, JButton> lazyGrid;
	static SocketClient client;
	static JTextArea txtArea;
	public ReversiClient (String title) {
		super(title);
	}
	public static void main(String[] args) {
		
		UIUtils ui = new UIUtils();
		
		ReversiClient frame = new ReversiClient ("Reversi: Remastered");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(800,800));
		JPanel empty = new JPanel();
		
		
		JPanel connectionDetails = new JPanel();
		JTextField host = new JTextField();
		host.setText("127.0.0.1");
		JTextField port = new JTextField();
		port.setText("3001");
		JButton connect = new JButton();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
		
		connect.setText("Connect");
		connectionDetails.add(host);
		connectionDetails.add(port);
		connectionDetails.add(connect);
		frame.add(connectionDetails, BorderLayout.NORTH);
		
		connect.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	int _port = -1;
		    	try {
		    		_port = Integer.parseInt(port.getText());
		    	}
		    	catch(Exception num) {
		    		System.out.println("Port not a number");
		    	}
		    	if(_port > -1) {
			    	client = SocketClient.connect(host.getText(), _port);
			    	client.registerListener(frame);
			    	//TODO Decide whether to show Client Name upon connection 
			    	client.postConnectionData();
			    	connect.setEnabled(false);
			    	connectionDetails.setVisible(false);

		    	}
		    }
		});
		
		int rows = 8;
		int cols = 8;
		Dimension gridDimensions = new Dimension(400,400);
		JPanel grid1 = new JPanel();
		grid1.setLayout(new GridLayout(rows,cols));
		grid1.setPreferredSize(gridDimensions);
		JPanel grid2 = new JPanel();
		grid2.setLayout(new GridLayout(rows, cols));
		grid2.setPreferredSize(gridDimensions);

		for(int i = 0; i < (rows*cols); i++) {
			JButton button = new JButton();
			button.setSize(new Dimension(2,2));

			int x = i % rows;

			int y = i/cols;

			String buttonText = String.format("%1$s:(%2$s, %3$s)", i, x, y);

			button.setText(buttonText);
			button.setBackground(Color.green);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					grid2.grabFocus();
				}
				
			});
			grid1.add(button);
		}
		lazyGrid = new HashMap<Point,JButton>();
		int i = 0;
		Dimension buttonSize = new Dimension(2,2);
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				JButton bt = new JButton();
				String buttonText = String.format("%1$s:(%2$s, %3$s)", i, x, y);
				bt.setText(buttonText);
				bt.setLocation(x, y);
					Point p = new Point(x, y);
					lazyGrid.put(p, bt);
					bt.setBackground((p == testPoint)?Color.red:Color.green);
				bt.setSize(buttonSize);
				bt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton me = ((JButton)e.getSource());
						
						String t = me.getText();
						if(t.equalsIgnoreCase("|")) {
							me.setText("O");
							me.setBackground(Color.yellow);
						}
						else {
							me.setText("|");
							me.setBackground(Color.red);
						}
						
						

						grid2.grabFocus();
					}
					
				});
				grid2.add(bt);
				i++;
			}
		}
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("");
		txtArea = textArea;
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());
		chatArea.add(textArea, BorderLayout.CENTER);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
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
					textField.setText("");
				}
			}
			
		});
		userInput.add(textField);
		userInput.add(b);
		chatArea.add(userInput, BorderLayout.SOUTH);
		
		frame.add(chatArea, BorderLayout.CENTER);
		frame.add(grid2, BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
		
		setKeyBindings(grid2.getInputMap(), grid2.getActionMap());
		grid2.setFocusable(true);
		grid2.grabFocus();
	}
	
	public static void setKeyBindings(InputMap im, ActionMap am) {
		
		im.put(KeyStroke.getKeyStroke("pressed UP"), "UAD");
		im.put(KeyStroke.getKeyStroke("pressed DOWN"), "DAD");
		im.put(KeyStroke.getKeyStroke("pressed LEFT"), "LAD");
		im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "RAD");
		
		im.put(KeyStroke.getKeyStroke("released UP"), "UAU");
		im.put(KeyStroke.getKeyStroke("released DOWN"), "DAU");
		im.put(KeyStroke.getKeyStroke("released LEFT"), "LAU");
		im.put(KeyStroke.getKeyStroke("released RIGHT"), "RAU");
		
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "SPACE");
		

		am.put("UAD", new MoveAction(true, 0, -1));
		am.put("DAD", new MoveAction(true, 0, 1));
		am.put("LAD", new MoveAction(true, -1,0));
		am.put("RAD", new MoveAction(true, 1, 0));

		am.put("UAU", new MoveAction(false, 0, -1));
		am.put("DAU", new MoveAction(false, 0, 1));
		am.put("LAU", new MoveAction(false, -1,0));
		am.put("RAU", new MoveAction(false, 1, 0));
	}

	@Override
	public void onReceived(boolean isOn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceivedMessage(String msg) {
		// TODO Auto-generated method stub
		if(txtArea != null) {
            txtArea.append(msg);
            txtArea.append(System.lineSeparator());
        }
	}
	@Override
	public void onReceivedTilePlacement(int clientId, int x, int y) {
		// TODO Auto-generated method stub
		
	}
	/*
	@Override
	public void onReceiveUpdateCell(int playId, Point coord) {
		// TODO Auto-generated method stub
		if(ReversiClient.lazyGrid != null) {
			grid.setColor(playerId, coord.x, coord.y, color, true);
			grid.setSelection(playerId, coord.x, coord.y, true);
		}
	}
	*/
	List<String> users = new ArrayList<String>();
	JTextArea userArea = new JTextArea();
	@Override
	public void onReceiveConnection(String name, boolean isConnected) {
		// TODO Auto-generated method stub
		if(isConnected) {
			users.add(name);
		}
		else {
			users.remove(name);
		}
		userArea.setText("");
		for(int i = 0; i < users.size(); i++) {
			userArea.append(users.get(i));
			userArea.append(System.lineSeparator());
		}
		
	}
}
class MoveAction extends AbstractAction{
	private static final long serialVersionUID = 5137817329873449021L;
	int x,y;
	boolean pressed = false;
	MoveAction(boolean pressed, int x, int y){
		this.x = x;
		this.y = y;
		this.pressed = pressed;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!pressed) {
			return;
		}
		ReversiClient.lazyGrid.forEach((point, button)->{
			button.setBackground(Color.white);
		});
		Point previous = new Point(ReversiClient.testPoint.x, ReversiClient.testPoint.y);
		Point next = new Point(previous.x, previous.y);
		if(x != 0) {
			next.x += x;
		}
		if(y != 0) {
			next.y += y;
		}
		System.out.println("Next Coord: " + next);
		if(ReversiClient.lazyGrid.containsKey(next)) {
			ReversiClient.lazyGrid.get(next).setBackground(Color.red);
			ReversiClient.testPoint = next;
		}
		else {
			ReversiClient.lazyGrid.get(previous).setBackground(Color.red);
		}
		System.out.println("TestPoint Coord: " + ReversiClient.testPoint);
	}
}