package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameObjectContainer;
import tp1.logic.Position;
import tp1.view.Messages;

//Mario class extiende de GameObject, es su hijo
public class Mario extends GameObject {

	//TODO fill your code
	private final Game game;
	private int direccion; // -1 izquierda, 1 derecha
	private boolean big;
	private boolean facingRight; //true si mira a la derecha, false si mira a la izquierda

	//Constructor de Mario
	public Mario(Game game, Position position){
		super(position);
		this.game = game;
		this.direccion = 1; //Por defecto mira a la derecha
		this.big = true; //Por defecto es grande
		this.facingRight = true;
	}	
	
	/**
	 *  Implements the automatic update	
	 */
	@Override
	public void update() {
		//TODO fill your code
		//1: Movimiento horizontal
		Position newPos = pos.move(0, direccion);

		if(canMoveTo(newPos)){
			pos = newPos;
		}else{
			direccion = -direccion; //Cambia de direccion si se choca
			facingRight = (direccion == 1);
		}
		//2 Gravedad (cuando se cae o no hay suelo)
		applyGravity();
	}

	private void applyGravity(){
		Position debajo = pos.move(1,0);
		//Si no hay suelo cae
		 while (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            pos = debajo;
            debajo = pos.move(1,0);
        }
		//Si se sale del tablero muere
		if(!pos.isValidPosition()){
			game.marioDies();
		}
	}

	private boolean canMoveTo(Position position){
		//Comprueba si la posicion es valida y no hay ningun Land en esa posicion
		return position.isValidPosition() && !game.getGameObjects().isSolid(position);
	}

	@Override
	public String getIcon() {
		if(facingRight){
			return Messages.MARIO_RIGHT;
		}
		else{
			return Messages.MARIO_LEFT; 
		}
	}	
	 public GameObjectContainer getGameObjects() {
        return game.getGameObjects();
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
			Position above = new Position(pos.getRow() - 1, pos.getCol());
			return above.equals(position);
		}
		return false;
	}

}
