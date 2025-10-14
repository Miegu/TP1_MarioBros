package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
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
	private ActionList accionesPendientes;
	private boolean hasMovedThisTurn; //Para saber si ya se ha movido en el turno actual;
	private boolean isFalling;

	//Constructor de Mario
	public Mario(Game game, Position position){
		super(position);
		this.game = game;
		this.direccion = 1; //Por defecto mira a la derecha
		this.big = true; //Por defecto es grande
		this.facingRight = true;
		this.accionesPendientes = new ActionList();
		this.hasMovedThisTurn = false;
	}	
	
	/**
	 *  Implements the automatic update	
	 */
	@Override
	public void update() {
		//TODO fill your code
		boolean playerHasActions = !accionesPendientes.isEmpty();
		isFalling = false;
		hasMovedThisTurn = false;
		//1. Primero procesamos las acciones
		if(playerHasActions) processPlayerActions();

		//2. Si no se ha movido, movimiento automatico
		if(!hasMovedThisTurn){
			performAutomaticMovement();
		}

		//3 Aplicar la gravedad
		applyGravity();

		//4. Chequea? Checkea? Comprueba si interactua con algo (Wombat? MAybe)
		game.doInteractionsFrom(this);
		
	}

	//Procesa las acciones del jugador
	private void processPlayerActions(){
		if(accionesPendientes.isEmpty()){
			return;
		}

		for(Action action : accionesPendientes.getActions()){
			executeAction(action);
		}

		accionesPendientes.clear();
	}

	private void applyGravity(){
		Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
		//Si se sale del tablero muere
		if(!pos.isValidPosition()){
			game.marioDies();
		}
		//Si no hay suelo cae
		 if (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            pos = debajo;
			isFalling = true;
			hasMovedThisTurn = true;
        }
		
	}

	//Comprueba si Mario esta en el suelo
	 private boolean isOnGround() {
        Position below = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
        return !below.isValidPosition() || game.getGameObjects().isSolid(below);
    }

	//Ejecuta una accion
	private void executeAction(Action action){
		Position newPos;

		switch(action){
			case LEFT:
				newPos = pos.move(action.getY(), action.getX());
				if(canMoveTo(newPos)){
					pos = newPos;
					hasMovedThisTurn = true;
				}
				direccion = -1;
				facingRight= false;
				break;

			case RIGHT:
				newPos = pos.move(action.getY(), action.getX());
				if(canMoveTo(newPos)){
					pos = newPos;
					hasMovedThisTurn = true;
				}
				direccion = 1;
				facingRight= true;
				break;

			case UP:
				newPos = pos.move(action.getY(), action.getX());
				if(canMoveTo(newPos)){
					pos = newPos;
					hasMovedThisTurn = true;
				}
				break;
			case DOWN:
				if(isOnGround()){
					direccion = 0; //No se mueve horizontalmente mientras cae
				}else{
					while(!isOnGround()){
						applyGravity();
					}
				}
				break;
			
			case STOP:
				direccion = 0;
				break;
		}

	}

	// Movimiento automatico version nueva y renovada

	private void performAutomaticMovement(){
		if(direccion == 0) return; //Estamos en STOP;

		Position newPos = pos.move(0, direccion);

		if(canMoveTo(newPos)){
			pos = newPos;
		}else{
			direccion = -direccion; //Cambia de direccion si se choca
			facingRight = (direccion == 1);
		}
	}
	//INteractua con la puerta de salida
	public boolean interactWith(ExitDoor door){
		if(this.pos.equals(door.getPosition())){
			game.marioExited();
			return true;
		}
		return false;
	}

	// interactua con el Gomba? Siempre les he dicho Wombats
	public boolean interactWith(Goomba goomba){

		if(!this.pos.equals(goomba.getPosition())){
			return false;
		}
		//Esta saltando sobre el?
		

		if(isFalling){
			goomba.receiveInteraction(this);
			game.addPoints(100);
			return true;
		}
	    if (big) {
            //No muere, pero se hace pequeño
            big = false;
            goomba.receiveInteraction(this); //El goomba muere RIP
            game.addPoints(100);
        } else {
           //Muere, logicamente...
            goomba.receiveInteraction(this); //Pero aun así muere el goomba?? @PETA
            game.marioDies();
           }
		return true;
	}


	/**
	 * Añadir Accion a la lista de acciones
	 * 
	 */
	public void addAction(Action action){
		accionesPendientes.addAction(action);
	}
	private boolean canMoveTo(Position position){
		//Comprueba si la posicion es valida y no hay ningun Land en esa posicion
		return position.isValidPosition() && !game.getGameObjects().isSolid(position);
	}

	@Override
	public String getIcon() {
		if(direccion == 0){
			return Messages.MARIO_STOP;
		}else if(facingRight){
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
