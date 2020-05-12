import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameEngine extends _GameEngine{
	
	public static List<Player> players = new ArrayList<Player>();
	GameClient ui;
	SocketClient client;
	public void connect(String host, int port, String clientName) {
		client = SocketClient.connect(host, port);
		if(client != null && SocketClient.isConnected) {
			client.setClientName(clientName);
		}
	}
	public void SetUI(GameClient ui) {
		this.ui = ui;
	}
	@Override
	protected void Awake() {
		// TODO Auto-generated method stub
		System.out.println("Game Engine Awake");
		
	}

	@Override
	protected void OnStart() {
		// TODO Auto-generated method stub
		System.out.println("Game Engine Start");
		for(int i = 0; i < 2; i++) {
			Player p = new Player();
			players.add(p);
			System.out.println("Added player " + i);
		}
	}

	@Override
	protected void Update() {
		
	}

	@Override
	protected void End() {
		// TODO Auto-generated method stub
		System.out.println("Game Engine End");
	}

	@Override
	protected void UILoop() {
		// TODO Auto-generated method stub
		if(ui == null) {
			return;
		}
	}

}