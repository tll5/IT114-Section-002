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
	private OnReceive listener;
	public void registerListener(OnReceive listener) {
		this.listener = listener;
	}
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

	public void setClientName(String name) {
		Payload p = new Payload();
		p.setPayloadType(PayloadType.CONNECT);
		toServer.add(p);
	}
	

	public void start() throws IOException {
		if(server == null) {
			return;
		}
		System.out.println("Client Started");
		try(	ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());){
			Thread inputThread = new Thread() {
				@Override
				public void run() {
					try {
						while( !server.isClosed()) {
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
			inputThread.start();
			
			Thread fromServerThread = new Thread() {
				@Override
				public void run() {
					try {
						Payload p;
						while(!server.isClosed() && (p = (Payload)in.readObject()) != null) {
							fromServer.add(p);
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
			while(!server.isClosed()) {
				Thread.sleep(50);
			}
			System.out.println("Exited loop");
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
		toServer.add(payload);
	}
	
	public void doClick(boolean isOn) {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.SWITCH);
		payload.IsOn(isOn);
		toServer.add(payload);
	}
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
		String msg = "";
		switch(payload.getPayloadType()) {
		case CONNECT:
			msg = String.format("Player \"%s\" connected", payload.getMessage());
			System.out.println(msg);
			if(listener != null) {
				listener.onReceivedMessage(msg);
			}
			break;
		case DISCONNECT:
			msg = String.format("Player \"%s\" connected", payload.getMessage());
			System.out.println(msg);
			if(listener != null) {
				listener.onReceivedMessage(msg);
			}
			break;
		case MESSAGE:
			System.out.println(
					String.format("%s", payload.getMessage())
			);
			if(listener != null) {
				listener.onReceivedMessage(payload.getMessage());
			}
			break;
		case TILE_CHECK:
			System.out.println(
					String.format("Player \"%s\" cannot act during the opponent's turn", payload.getMessage())
			);
			break;
		case STATE_SYNC:
			System.out.println("Synching");
			/*
			if(onReceiveListener != null) {
				onReceiveListener.onReceiveUpdateCell(payload.getPlayerId(), payload.getCoord());
			}
			*/
			break;
		case SWITCH:
			System.out.println("Switching");
			if (listener != null) {
				listener.onReceived(payload.IsOn());
				listener.onReceivedMessage(
						String.format("%s turned the button %s", 
								payload.getMessage(),
								payload.IsOn()?"On":"Off")
				);
			}
			/*
			if (listener != null) {
				listener.onReceived(payload.IsOn());
			}
			*/
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
	/*
	public void setClientName(String clientName) {
		// TODO Auto-generated method stub
		
	}
	*/
}
interface OnReceive {
	void onReceived(boolean isOn);
	void onReceivedMessage(String msg);
	void onReceiveConnection(String name, boolean isConnected);
	void onReceivedTilePlacement(int clientId, int x, int y);
	//void onReceiveUpdateCell(int playId, Point coord);
}