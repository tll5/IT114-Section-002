import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SocketClient {
	public static boolean isConnected;
	Socket server;
	//GameClient gc; //remove?
	private OnReceiveMessage listener;
	public void registerListener(OnReceiveMessage listener) {
		this.listener = listener;
	}
	//public static boolean isConnected = false;
	private Queue<Payload> toServer = new LinkedList<Payload>();
	private Queue<Payload> fromServer = new LinkedList<Payload>();
	//public static boolean isRunning = false;
	/*
	public void SetGameClient(GameClient gc) {
		this.gc = gc;
	}
	*/

	public void _connect(String address, int port) {
		try {
			server = new Socket(address, port);
			System.out.println("Player connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
	public void setClientName(String name) {
		//we should only really call this once
		//we should have a name, let's tell our server
		Payload p = new Payload();
		//we can also default payloadtype in payload
		//to a desired value, though it's good to be clear
		//what we're sending
		p.setPayloadType(PayloadType.CONNECT);
		//p.setMessage(name);
		
		//p.setClientName(name);
		
		//out.writeObject(p);
		toServer.add(p);
	}
	*/

	public void start() throws IOException {
		if(server == null) {
			return;
		}
		System.out.println("Client Started");
		//isRunning = true;
		//listen to console, server in, and write to server out
		try(	ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());){
			//Thread to listen for keyboard input so main thread isn't blocked
			Thread inputThread = new Thread() {
				@Override
				public void run() {
					try {
						while( !server.isClosed()) {
							//we're going to be taking payloads off the queue
							//and feeding them to the server
							Payload p = toServer.poll();
							if(p != null) {
								out.writeObject(p);
							}
							else {
								try {
									Thread.sleep(8);
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					catch(Exception e) {
						e.printStackTrace();
						System.out.println("Client shutdown");
					}
					finally {
						close();
					}
				}
			};
			inputThread.start();//start the thread
			
			//Thread to listen for responses from server so it doesn't block main thread
			Thread fromServerThread = new Thread() {
				@Override
				public void run() {
					try {
						Payload p;
						//while we're connected, listen for payloads from server
						while(!server.isClosed() && (p = (Payload)in.readObject()) != null) {
							//System.out.println(fromServer);
							fromServer.add(p);
							//processPayload(fromServer);
						}
						System.out.println("Stopping server listen thread");
					}
					catch (Exception e) {
						if(!server.isClosed()) {
							e.printStackTrace();
							System.out.println("Server closed connection");
						}
						else {
							System.out.println("Connection closed");
						}
					}
					finally {
						close();
					}
				}
			};
			fromServerThread.start();
	
			Thread payloadProcessor = new Thread(){
				@Override
				public void run() {
					while(!server.isClosed()) {
						Payload p = fromServer.poll();
						if(p != null) {
							processPayload(p);
						}
						else {
							try {
								Thread.sleep(8);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			};
			payloadProcessor.start();
			//Keep main thread alive until the socket is closed
			//initialize/do everything before this line
			while(!server.isClosed()) {
				Thread.sleep(50);
			}
			System.out.println("Exited loop");
			//System.exit(0);//force close
			//TODO implement cleaner closure when server stops
			//without this, it still waits for input before terminating
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
	
	public void postConnectionData() {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.CONNECT);
		//payload.IsOn(isOn);
		toServer.add(payload);
	}
	
	public void doClick(boolean isOn) {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.SWITCH);
		payload.IsOn(isOn);
		toServer.add(payload);
	}
	//Tile Check for the turns to commence and function properly
	public void doTileCheck(boolean yn ) {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.TILE_CHECK);
		toServer.add(payload);
	}

	public void sendMessage(String message) {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.MESSAGE);
		payload.setMessage(message);
		toServer.add(payload);
	}
	
	
	private void processPayload(Payload payload) {
		System.out.println(payload);
		switch(payload.getPayloadType()) {
		case CONNECT:
			System.out.println(
					String.format("Player \"%s\" connected", payload.getMessage())
			);
			break;
		case DISCONNECT:
			System.out.println(
					String.format("Player \"%s\" disconnected", payload.getMessage())
			);
			break;
		case MESSAGE:
			System.out.println(
					String.format("%s", payload.getMessage())
			);
			break;
		case TILE_CHECK:
			System.out.println(
					String.format("Player \"%s\" cannot act during the opponent's turn", payload.getMessage())
			);
			break;
		case STATE_SYNC:
			System.out.println("Synching");
			break;
		case SWITCH:
			System.out.println("Switching");
			if (listener != null) {
				listener.onReceived(payload.IsOn());
			}
			break;	
		default:
			System.out.println("Unhandled payload type: " + payload.getPayloadType().toString());
			break;
		}
	}
	private void close() {
		if(server != null) {
			try {
				server.close();
				System.out.println("Closed socket");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static SocketClient connect(String host, int port) {
		SocketClient client = new SocketClient();
		client._connect(host, port);
		Thread clientThread = new Thread() {
			@Override
			public void run() {
				try {
					client.start();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		clientThread.start();
		try {
			Thread.sleep(50);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return client;
	}
	public static void main(String[] args) {
		SocketClient client = new SocketClient();
		client.connect("127.0.0.1", 3002);
		//System.out.println("Client is connected and started");
		
		try {
			//if start is private, it's valid here since this main is part of the class
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void setClientName(String clientName) {
		// TODO Auto-generated method stub
		
	}

}
interface OnReceiveMessage {
	void onReceived(boolean isOn);
}