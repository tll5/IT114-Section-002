import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;

/**
 * Starting point of the application to group up all the classes
 * @author MattT
 *
 */
public class ReversiGrid {
	public static void main(String[] ar) {
		Client c = new Client();
		Server s = new Server();
	}
}
/**
 * Represents a single coordinate of a grid
 * Used a class so we can store more data
 * @author MattT
 *
 */

class Cell extends JButton{
	private static final long serialVersionUID = 1L;
	private int cellID = -1;
	private Point coord;
	private int playerID = -1;
	private Dimension sizeCache;
	public Cell() {
		super();
	}
	public Cell(int id, Point coord) {
		super();
		cellID = id;
		this.coord = coord;
		this.setBackground(Color.white);
	}
	
	public int getCellID() {
		return cellID;
	}
	public void setCellID(int cellID) {
		this.cellID = cellID;
	}
	
	
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public Point getCoord() {
		return coord;
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		//grab the current dimensions
		sizeCache = this.getSize();
	    g.fillRect(0, 0, sizeCache.width, sizeCache.height);
	}
	public int player = -1;//-1 is untouched
	//We can add a point or x,y to have a local ref of our coordinate
	public Point mySpot = new Point(0,0);//init later
	public Cell(Point spotCache) {
		mySpot = spotCache;
	}
	public void setPlayerSelection(int playerIndex) {
		if(player < 0) {
			player = playerIndex;
		}
		else {
			System.out.println("Someone's already at this coordinate");
		}
	}
	public void reset() {
		player = -1;
	}
	

}
class Grid{
	Cell[][] grid;
	Dimension size;
	public Grid(int cols, int rows) {
		grid = new Cell[cols][rows];
		size = new Dimension(cols, rows);
		for(int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				grid[x][y] = new Cell(new Point(x,y));
			}
		}
		initGrid();
	}
	private void initGrid() {
		int i = 0;
		for(int row = 0; row < size.width; row++) {
			for(int col = 0; col < size.height; col++) {
				grid[row][col] = new Cell(i, new Point(row, col));
				i++;
			}
		}
	}
	void init() {
		Cell[][] boardInit = new Cell[8][8];
		boardInit[4][4] = new Cell();
		boardInit[4][5] = new Cell();
		boardInit[5][4] = new Cell();
		boardInit[5][5] = new Cell();
		boards.add(boardInit);
	}
	List<Cell[][]> boards = new ArrayList<Cell[][]>();
	void pickBoard() {
		Random rand = new Random();
		Cell[][] presetGrid = boards.get(rand.nextInt(boards.size()));
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
	private void broadcast(Payload payload) {
		// TODO Auto-generated method stub
		broadcast(payload);
	}
	public Dimension getSize() {
		return size;
	}
	public void draw() {
		for(int row = 0; row < size.width; row++) {
			for(int col = 0; col < size.height; col++) {
				grid[row][col].repaint();
			}
		}
	}
	/*
	public Cell getCell(int x, int y) {
		return grid[x][y];
	}
	*/
	public Cell getCell(Point p) {
		return getCell(p.x, p.y);
	}
	public String getPlayerID(String clientName) {
		return clientName;
	}
	public void updateCell(int x, int y, String something) {
		//TODO do something with cell via param(s)
		try {
			//for sake of example just trying to convert string to int since
			//we're passing player index/id as a string
			getCell(x, y).player = Integer.parseInt(something);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Cell " + x + ", " + y + ": Something happened: " + something);
	}
	public void resetGrid() {
		for(int x = 0; x < grid[0].length; x++) {
			for(int y = 0; y < grid[1].length; y++) {
				//reset each cell
				grid[x][y].reset();
			}
		}
	}
	
	public boolean setSelection(int playerId, int x, int y, boolean allowOverwrite) {
		try {
			Cell c = grid[x][y];
			if(!allowOverwrite) {
				if(c.getPlayerID() != playerId) {
					return false;
				}
			}
			c.setPlayerID(playerId);
		}
		catch(Exception e) {
			System.out.println("Likely coordinates are out of bounds");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean setColor(int playerId, int x, int y, Color color, boolean allowOverwrite) {
		try {
			Cell c = grid[x][y];
			if(!allowOverwrite) {
				if(c.getPlayerID() != playerId) {
					return false;
				}
			}
			c.setBackground(color);
			c.setPlayerID(playerId);
		}
		catch(Exception e) {
			System.out.println("Likely coordinates are out of bounds");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public Cell getCell(int x, int y) {
		return grid[x][y];
	}
}

class Client{
	Grid grid;
	public Client() {
		grid = new Grid(8,8);
	}
	public void processPayload(Payload p) {
		//from server
		if(p.type == 1 /*Pick spot*/) {
			grid.updateCell(p.x, p.y, p.player+"");
		}
	}
}
class Server{
	Grid grid;
	public Server() {
		//must match client
		grid = new Grid(8,8);
	}
	public void broadcast(Payload p) {
		//Send to all
	}
	public void reply(int client, Payload p) {
		//send reply to one
	}
	public void processPayload(Payload p) {
		//from client
		
		if(p.type == 1 /*pick spot*/) {
			Cell cell = grid.getCell(p.x, p.y);
			if(cell.player < 0) {
				//OK, spot is vacant
				//broadcast move to players
				broadcast(p);
			}
			else {
				p.message = "Spot's taken, sorry";
				reply(p.clientId, p);
			}
		}
	}
}
