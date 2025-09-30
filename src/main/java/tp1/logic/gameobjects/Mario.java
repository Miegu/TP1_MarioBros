package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;

//Mario class extiende de GameObject, es su hijo
public class Mario extends GameObject {

	//TODO fill your code
	private Game game;
	private boolean facingRight;
	private boolean big;

	//Constructor de Mario
	public Mario(Game game, Position position){
		super(position);
		this.game = game;
		this.facingRight = true; //Por defecto mira a la derecha
		this.big = false; //Por defecto es pequeño
	}	
	
	/**
	 *  Implements the automatic update	
	 */
	@Override
	public void update() {
		//TODO fill your code
	}
	@Override
	public char getIcon() {
		if(facingRight){
			return 'M';
		}
		else{
			return 'W'; //W es Mario mirando a la izquierda
		}
	}	

	public boolean isBig(){
		return big;
	}	

	public void setBig(boolean big){
		this.big = big;
	}
	@Override
	public boolean isInPosition(Position position){
		if(pos.equals(position)){
			return true;
		}
		//Si Mario es grande, tambien ocupa la posicion de arriba
		if(big && pos.getRow() > 0){
			Position above = new Position(pos.getCol(), pos.getRow() - 1);
			return above.equals(position);
		}
		return false;
	}

}
