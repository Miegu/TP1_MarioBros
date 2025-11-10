package tp1.logic;

/**
 * Immutable class to encapsulate and manipulate positions in the game
 * board
 *
 */
public class Position {

    private final int col;
    private final int row;

    //Constructor
    public Position(int row, int col) {
        this.row = row;
        this.col = col;

    }
    //Metodos (que puede hacer)
    //Getters

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    public Position right() { 
        return new Position(this.row, this.col + 1); 
    }
    
    public Position left() { 
        return new Position(this.row, this.col - 1); 
    }
    
    public Position up() { 
        return new Position(this.row - 1, this.col); 
    }
    
    public Position down() { 
        return new Position(this.row + 1, this.col); 
    }

    //Metodo para moverse: devuelve una nueva posicion, Posicion actual + desplazamiento
    public Position move(int deltaRow, int deltaCol) {
        return new Position(row + deltaRow, col + deltaCol);
    }

    public Position move(Action action) {
        if (action == null) return this;
        
        switch (action) {
            case LEFT:  return left();
            case RIGHT: return right();
            case UP:    return up();
            case DOWN:  return down();
            case STOP:  return this;  // No se mueve
            default:    return this;
        }
    }

    //Comprobar si la posicion es valida
    public boolean isValidPosition() {
        return col >= 0 && col < Game.DIM_X
                && row >= 0 && row < Game.DIM_Y;
    }

    //Comparar Posiciones
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return col == position.col && row == position.row;
    }
    public boolean equals(Position other) {
        if (other == null) return false;
        return this.col == other.col && this.row == other.row;
    }

    @Override
    public String toString() {
        return "(" + col + "," + row + ")";
    }
}
