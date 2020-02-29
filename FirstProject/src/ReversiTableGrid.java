import java.util.*;
class ReversiTableGrid {
	
	public class Coordinate {
		int x;
		int y;
		Coordinate(int xCord, int yCord) {
			x = xCord;
			y = yCord;
		}
		
		@Override
		public String toString() {
			return "["+x+", "+y+"]";
		}
		@Override
		public boolean equals(Object obj) {
			return obj.hashCode()==this.hashCode();
		}
		@Override
		public int hashCode() {
			return Integer.parseInt(x+""+y);
		}
	}
	private char[][] reversigrid;
	int white;
	int black;
	private final char boardRef [] = new char [] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	
	public ReversiTableGrid() {
		char[][] tab = new char[][] {
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'W', 'B', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'B', 'W', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
			{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x',},
		};
	}

	public static void main(String args[])
	{
		ReversiTableGrid r = new ReversiTableGrid();
		System.out.println(r);
	}
}
