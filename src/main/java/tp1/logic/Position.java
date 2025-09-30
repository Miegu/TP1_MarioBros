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
	public Position(int row, int col){
		this.row = row;
		this.col = col;
		
	}
	//Metodos (que puede hacer)
	//Getters
	public int getCol(){
		return col;
	}

	public int getRow(){
		return row;
	}
	
	//Metodo para moverse: devuelve una nueva posicion, Posicion actual + desplazamiento
	public Position move(int deltaRow, int deltaCol){
		return new Position( row + deltaRow, col + deltaCol);
	}

	//Comprobar si la posicion es valida

	public boolean isValidPosition(){
		return col >= 0 && col < Game.DIM_X &&
				row >= 0 && row < Game.DIM_Y;
	}

	//Comparar Posiciones
	public boolean equals(Position other){
		return this.col == other.col && this.row == other.row;
	}

	@Override
    public String toString() {
        return "(" + col + "," + row + ")";
    }
}
