package tp1.logic;

/**
 * 
 * TODO: Immutable class to encapsulate and manipulate positions in the game board
 * 
 */
public class Position {

	private int col;
	private int row;

	//TODO fill your code
	//Constructor
	public Position(int col, int row){
		this.col = col;
		this.row = row;
	}
	//Metodos (que puede hacer)
	//Getters
	public int getCol(){
		return col;
	}

	public int getRow(){
		return row;
	}
	
	public void setCol(int col){
		this.col = col;
	}
	public void setRow(int row){
		this.row = row;
	}
	//Metodo para moverse: devuelve una nueva posicion, Posicion actual + desplazamiento
	public Position move(int dcol,int drow){
		return new Position(col + dcol, row + drow);
	}

	//Comprobar si la posicion es valida

	public boolean isValidPosition(){
		return col >= 0 && col < Game.DIM_X &&
				row >= 0 && row < Game.DIM_Y;
	}
}
