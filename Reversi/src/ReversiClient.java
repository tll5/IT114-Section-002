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
import java.util.HashMap;

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
		//frame.setMinimumSize(new Dimension(600,600));
		//empty panel we'll use as a spacer for now
		JPanel empty = new JPanel();
		
		
		JPanel connectionDetails = new JPanel();
		JTextField host = new JTextField();
		host.setText("127.0.0.1");
		JTextField port = new JTextField();
		port.setText("3001");
		JButton connect = new JButton();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add a window listener
		frame.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				// before we stop the JVM stop the example
				//client.isRunning = false;
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
		//Create two sample grids to compare adding elements
		JPanel grid1 = new JPanel();
		//set gridlayout pass in rows and cols
		grid1.setLayout(new GridLayout(rows,cols));
		grid1.setPreferredSize(gridDimensions);
		//grid1.setMaximumSize(gridDimensions);
		JPanel grid2 = new JPanel();
		//set gridlayout pass in rows and cols
		grid2.setLayout(new GridLayout(rows, cols));
		grid2.setPreferredSize(gridDimensions);
		//grid2.setMaximumSize(gridDimensions);
		//JTextField textField = new JTextField();
		//grid layout creation (full layout control)
		for(int i = 0; i < (rows*cols); i++) {
			JButton button = new JButton();
			button.setSize(new Dimension(2,2));
			//convert to x coordinate
			int x = i % rows;
			//convert to y coordinate
			int y = i/cols;
			//%1 first param, %2 second param, etc
			String buttonText = String.format("%1$s:(%2$s, %3$s)", i, x, y);
			//show index and coordinate details on button
			button.setText(buttonText);
			
			button.setBackground(Color.green);
			//create an action to perform when button is clicked
			//override the default actionPerformed method to tell the code how to handle it
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//textField.setText(((JButton)e.getSource()).getText());
					
					//give focus back to grid2 for navigation sample
					grid2.grabFocus();
				}
				
			});
			grid1.add(button);
		}
		//can omit if not doing navigation sample
		lazyGrid = new HashMap<Point,JButton>();
		//keep if using Random, otherwise can omit
		//Random random = new Random();
		int i = 0;
		Dimension buttonSize = new Dimension(2,2);
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				JButton bt = new JButton();
				//%1 first param, %2 second param, etc
				String buttonText = String.format("%1$s:(%2$s, %3$s)", i, x, y);
				bt.setText(buttonText);
				bt.setLocation(x, y);
				//point to button map for easy button reference in navigation sample
				//can omit these related lines if it's not relevant to you
					Point p = new Point(x, y);
					lazyGrid.put(p, bt);
					//set background color based on this point matching our testPoint
					bt.setBackground((p == testPoint)?Color.red:Color.green);
					//uncomment if you want random colors per button
					//bt.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
				//end potential omit section
				bt.setSize(buttonSize);
				//create an action to perform when button is clicked
				//override the default actionPerformed method to tell the code how to handle it
				bt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//set the textfield value to the text value of the button to show clicked coordinate
						
						//textField.setText(((JButton)e.getSource()).getText());
						//set clicked button to red
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
						
						
						//give focus back to grid2 for navigation sample
						grid2.grabFocus();
					}
					
				});
				//add the button to grid2
				grid2.add(bt);
				//increment our index to demo the order the buttons are added
				i++;
			}
		}
		//add grid1 sample to left
		//frame.add(grid1, BorderLayout.WEST);
		//add empty space to prevent the grids from visually merging initially
		//TODO Adding ChatIntegration 
		JTextArea textArea = new JTextArea();
		//don't let the user edit this directly
		textArea.setEditable(false);
		textArea.setText("");
		txtArea = textArea;
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());
		//add text area to chat area
		chatArea.add(textArea, BorderLayout.CENTER);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel userInput = new JPanel();
		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100,30));
		//setup submit button
		JButton b = new JButton();
		b.setPreferredSize(new Dimension(100,30));
		b.setText("Send");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = textField.getText();
				if(message.length() > 0) {
					//append a newline and the text from the textfield
					//to that textarea (simulate simple chatroom)
					client.sendMessage(message);
					textField.setText("");
				}
			}
			
		});
		//add textfield and button to panel
		userInput.add(textField);
		userInput.add(b);
		chatArea.add(userInput, BorderLayout.SOUTH);
		
		frame.add(chatArea, BorderLayout.CENTER);
		//add grid2 sample to right
		frame.add(grid2, BorderLayout.WEST);
		//add output field to bottom
		//frame.add(textField, BorderLayout.SOUTH);
		//resize based on elements applied to layout
		frame.pack();
		frame.setVisible(true);
		
		//set keybindings for navigation sample, may omit this and related method/function
		//if not useful
		setKeyBindings(grid2.getInputMap(), grid2.getActionMap());
		grid2.setFocusable(true);
		grid2.grabFocus();
	}

	//keybindings sample to show grid movement
	public static void setKeyBindings(InputMap im, ActionMap am) {
		
		//bind key actions to action map
		im.put(KeyStroke.getKeyStroke("pressed UP"), "UAD");
		im.put(KeyStroke.getKeyStroke("pressed DOWN"), "DAD");
		im.put(KeyStroke.getKeyStroke("pressed LEFT"), "LAD");
		im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "RAD");
		
		im.put(KeyStroke.getKeyStroke("released UP"), "UAU");
		im.put(KeyStroke.getKeyStroke("released DOWN"), "DAU");
		im.put(KeyStroke.getKeyStroke("released LEFT"), "LAU");
		im.put(KeyStroke.getKeyStroke("released RIGHT"), "RAU");
		
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "SPACE");
		
		//bind Action to Action map
		am.put("UAD", new MoveAction(true, 0, -1));
		am.put("DAD", new MoveAction(true, 0, 1));
		am.put("LAD", new MoveAction(true, -1,0));
		am.put("RAD", new MoveAction(true, 1, 0));
		//technically we don't need this, we're just listening for keydown
		//but include for a complete example
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
}
//Create a move action we can trigger on key press
class MoveAction extends AbstractAction{
	private static final long serialVersionUID = 5137817329873449021L;
	//passed in direction we want to move
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
			//in this example we only care about pressed = true
			//so we return here if it's false (when the key is up)
			return;
		}
		//reset all buttons to white background
		ReversiClient.lazyGrid.forEach((point, button)->{
			button.setBackground(Color.white);
		});
		
		//This line passes reference to testPoint, so it doesn't revert correctly
		//when it moves outside of the grid
		//uncomment the below line and comment out line 175 to see
		//Point previous = BasicGrid.testPoint;
		//Point next = previous;
		
		//This creates a new point so we don't affect the original until we want to
		Point previous = new Point(ReversiClient.testPoint.x, ReversiClient.testPoint.y);
		Point next = new Point(previous.x, previous.y);
		if(x != 0) {
			next.x += x;
		}
		if(y != 0) {
			next.y += y;
		}
		System.out.println("Next Coord: " + next);
		//check if point exists in our grid mapping, if so update the position's color
		if(ReversiClient.lazyGrid.containsKey(next)) {
			ReversiClient.lazyGrid.get(next).setBackground(Color.red);
			ReversiClient.testPoint = next;
		}
		else {
			//reset color for previous point
			ReversiClient.lazyGrid.get(previous).setBackground(Color.red);
		}
		System.out.println("TestPoint Coord: " + ReversiClient.testPoint);
	}
}