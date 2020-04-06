import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class GameClient extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6748325367132904432L;
	
	public static boolean isRunning = true;
	//Player player = new Player();
	
	UIUtils ui = new UIUtils();
	
	static HashMap<String, Component> components = new HashMap<String, Component>();
	static GameState gameState = GameState.LOBBY;
	public static JFrame myFrame;
	GameEngine ge;
	public GameClient() {
		
		
	}
	public void toggleRunningState(boolean s) {
		isRunning = s;
	}
	/***
	 * Looks up a component by name and changes visibility based on the toggle boolean value
	 * @param name
	 * Name of component key
	 * @param toggle
	 * Boolean for isVisible
	 */
	void toggleComponent(String name, boolean toggle) {
		if(components.containsKey(name)) {
			components.get(name).setVisible(toggle);
		}
	}
	/***
	 * Based on Client State we'll hide/show groups of components
	 * @param frame
	 */
	void ChangePanels() {
		switch(GameClient.gameState) {
			case GAME:
				toggleComponent("lobby", false);
				toggleComponent("game", true);
				//toggleComponent("score", true);
				//toggleComponent("scores", true);
				break;
			case LOBBY:
				toggleComponent("lobby", true);
				toggleComponent("game", false);
				//toggleComponent("score", false);
				//toggleComponent("scores", false);
				break;
			default:
				break;
		}
		myFrame.pack();
        myFrame.revalidate();
        myFrame.repaint();
	}
	/***
	 * Safely gets a UI element from components hashmap
	 * @param name
	 * @return null if key not exists
	 */
	public Component GetUIElement(String name) {
		if(components.containsKey(name)) {
			return components.get(name);
		}
		return null;
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		} catch (UnsupportedLookAndFeelException ex) {
		}
		JFrame frame = new JFrame("Reversi: Remastered");
		
		//Terminates program when we click the x (close) button)
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(800,800));
		GameClient.myFrame = frame;
		InitLobby();
		
		InitGameCanvas();
		
		GameClient gc = (GameClient)components.get("game");
		gc.ChangePanels();
		GameClient.myFrame.pack();
		GameClient.myFrame.setVisible(true);
		
	}
	public static GameClient InitGameCanvas() {
		//Game area
		JPanel playArea = new GameClient();
		//set the local reference of myFrame for easier use
		components.put("game", playArea);
		playArea.setPreferredSize(new Dimension(800,800));
		//playArea.getBounds()
		
		myFrame.add(playArea, BorderLayout.CENTER);
		return (GameClient)playArea;
	}
	public static void InitLobby() {
		JPanel lobby = new JPanel();
		components.put("lobby", lobby);
		lobby.setName("lobby");
		lobby.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel container = new JPanel();
		JTextField name = new JTextField(20);
		name.setText("Guest");
		JTextField host = new JTextField(15);
		host.setText("127.0.0.1");
		JTextField port = new JTextField(4);
		port.setText("3111");
		JButton connectButton = new JButton();
		connectButton.setText("Connect");
		connectButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        //your actions
		    	//host.getText().trim(), 
				//port.getText().trim()
		    	GameClient gc = (GameClient)components.get("game");
		    	if(gc != null) {
		    		gc.StartGameLoop(
		    				host.getText().trim(),
		    				Integer.parseInt(port.getText().trim()),
		    				name.getText().trim());
		    	}
		    }
		});
		
		JTextField message = new JTextField(60);
		message.setEditable(false);
		container.add(name);
		container.add(host);
		container.add(port);
		container.add(connectButton);
		lobby.setLayout(new BoxLayout(lobby, BoxLayout.PAGE_AXIS));
		lobby.add(container);
		
		lobby.add(message);
		
		components.put("lobby.message", message);
		//myFrame.add(scores, BorderLayout.NORTH);
		myFrame.add(lobby, BorderLayout.NORTH);
		
	}
	void StartGameLoop(String host, int port, String playername) {
		GameClient.gameState = GameState.GAME;
    	ChangePanels();
        toggleRunningState(true);
		//gc.run();
        ge = new GameEngine();
        ge.connect(host,port, playername);
        ge.SetUI(this);
        ge.start();
    	run();
	}
	public void UpdatePlayerName(String str) {
		for(int i = 0; i < GameEngine.players.size(); i++) {
			GameEngine.players.get(i).name = str;
		}
	}
	
	public void run() {
	/*while(GameEngine.isRunning) {
		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	}
}
enum GameState{
	LOBBY,
	GAME
}