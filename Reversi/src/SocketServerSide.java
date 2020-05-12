import java.awt.Point;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.*;

import com.google.gson.Gson;

public class SocketServerSide {
	int port = 3002;
	public static boolean isRunning = true;
	private List<ServerThread> clients = new ArrayList<ServerThread>();
	//We'll use a queue and a thread to separate our chat history
	Queue<String> messages = new LinkedList<String>();
	
	public GameState state = new GameState();
	public static long ClientID = 0;
	public synchronized long getNextId() {
		ClientID++;
		return ClientID;
	}
	public synchronized void toggleButton(Payload payload) {
		if(state.isButtonOn && !payload.IsOn()) {
			state.isButtonOn = false;
			broadcast(payload);
		}
		else if (!state.isButtonOn && payload.IsOn()) {
			state.isButtonOn = true;
			broadcast(payload);
		}
	}
	
	private void start(int port) {
		this.port = port;
		startQueueReader();
		//sample score save, why not here?
		//loadScore();
		//saveScore(1000);
		
		System.out.println("Waiting for opponent");
		try (ServerSocket serverSocket = new ServerSocket(port);) {
			while(SocketServerSide.isRunning) {
				try {
					Socket client = serverSocket.accept();
					System.out.println("Player connecting...");
					//Server thread is the server's representation of the client
					ServerThread thread = new ServerThread(client, this);
					thread.start();
					thread.setClientId(getNextId());
					//add client thread to list of clients
					clients.add(thread);
					System.out.println("Player added to players pool");
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				isRunning = false;
				Thread.sleep(50);
				System.out.println("closing server socket");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
	void init() {
		int[][] boardInit = new int[8][8];
		boardInit[4][4] = new Cell();
		boardInit[4][5] = new Cell();
		boardInit[5][4] = new Cell();
		boardInit[5][5] = new Cell();
		boards.add(boardInit);
	}
	List<int[][]> boards = new ArrayList<int[][]>();
	void pickBoard() {
		Random rand = new Random();
		int[][] presetGrid = boards.get(rand.nextInt(boards.size()));
		Grid grid = new Grid(8,8);
		syncReversi(grid);
	}
	public synchronized void syncReversi(Grid grid) {
		for(int col = 0; col < grid.getSize().width; col++) {
			for(int row = 0; row < grid.getSize().height; row++) {
				Cell cell = grid.getCell(col, row);
				Point p = cell.getCoord();
				int player = cell.getPlayerID();
				Payload payload = new Payload();
				payload.setPayloadType(PayloadType.STATE_SYNC);
				payload.setCoord(p);
				payload.setPlayerId(player);
				broadcast(payload);
			}
		}
	}
	*/
	/*
	public synchronized void setColor(ServerThread caller, Payload p) {
		boolean allowOverwrite = true;
		boolean success = Grid.setColor(
				p.getPlayerId(),
				p.getCoord().x,
				p.getCoord().y,
				//p.getColor(), 
				allowOverwrite);
		if(success) {
			broadcast(p);
		}
		else {
			caller.send("Either the coord is out of bounds or you're not allowed to overwrite another player");
		}
	}
	*/
	void loadScore() {
		try {
			Gson gson = new Gson();
			ScoreState ss = gson.fromJson(new FileReader("score.json"), ScoreState.class);
			long s = (long) ss.scores.get(0).score;
			System.out.println("Reversi score: " + s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void saveScore(int score){
		//false because we don't want to append
		//we want to update/replace the object
		//sample
		ScoreState ss = new ScoreState();
		ss.scores.add(new Score("You", 0));
		ss.scores.add(new Score("Opponent", 0));
		System.out.println(ss.toString());
		try(FileWriter writer = new FileWriter("score.json",false)){
			Gson gson = new Gson();
			writer.write(gson.toJson(ss));
			writer.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	void startQueueReader() {
		System.out.println("Preparing Queue Reader");
		Thread queueReader = new Thread() {
			@Override
			public void run() {
				String message = "";
				try(FileWriter write = new FileWriter("chathistory.txt", true)){
					while(isRunning) {
						message = messages.poll();
						if(message != null) {
							message = messages.poll();
							write.append(message);
							write.write(System.lineSeparator());
							write.flush();
						}
						sleep(50);
					}
				}
				catch(IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		queueReader.start();
		System.out.println("Started Queue Reader");
	}
	@Deprecated
	int getClientIndexByThreadId(long id) {
		for(int i = 0, l = clients.size(); i < l;i++) {
			if(clients.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
	public synchronized void broadcast(Payload payload, String name) {
		String msg = payload.getMessage();
		payload.setMessage(
				(name!=null?name:"[Name Error]") 
				+ (msg != null?": "+ msg:"")
		);
		broadcast(payload);
	}
	public synchronized void broadcast(Payload payload) {
		System.out.println("Sending message to " + clients.size() + " players");
		//TODO record message
		storeInFile(payload.getMessage());
		Iterator<ServerThread> iter = clients.iterator();
		while(iter.hasNext()) {
			ServerThread client = iter.next();
			boolean messageSent = client.send(payload);
			if(!messageSent) {
				iter.remove();
				System.out.println("Removed player " + client.getId());
			}
		}
	}
	public synchronized void broadcast(Payload payload, long id) {
		int from = getClientIndexByThreadId(id);
		String msg = payload.getMessage();
		payload.setMessage(
				(from>-1?"Client[" + from+"]":"unknown") 
				+ (msg != null?": "+ msg:"")
		);
		broadcast(payload);
		
	}
	public synchronized void broadcast(String message, long id) {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.MESSAGE);
		payload.setMessage(message);
		broadcast(payload, id);
	}
	void storeInFile(String message) {
		messages.add(message);
	}

	public static void main(String[] args) {
		int port = 3001;
		if(args.length >= 1) {
			String arg = args[0];
			try {
				port = Integer.parseInt(arg);
			}
			catch(Exception e) {
			}
		}
		System.out.println("Starting Server");
		SocketServerSide server = new SocketServerSide();
		System.out.println("Listening on port " + port);
		server.start(port);
		System.out.println("Server Stopped");
	}
}

class GameState {
	public static ClientState currentClientState;
	boolean isButtonOn = false;
}
enum ClientState {
	LOBBY,
	GAME
}
