package tp1.logic.gameobjects;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class MovingObject extends GameObjectNew {

	protected  int dir;
	protected boolean isFalling;
	
	
	public MovingObject (GameWorld game, Position pos, int dir) {
		super (game,pos);
		this.dir=dir;
		this.isFalling=false;
	}
	
	public void setDirection(int dir) {
        this.dir = dir;
    }
	
	
	//Método q nos dice si los objetos se pueden mover a una pos(pq es valida y no es solida)
	protected boolean canMoveTo(Position pos) {
        return pos.isValidPosition() && !game.getGameObjects().isSolid(pos);
    }
	
	//metodo q nos dice si hay suelo debajo de nuestros objetos
	protected boolean isOnGround() {
        Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
        return !debajo.isValidPosition() || game.getGameObjects().isSolid(debajo);
    }

	
	//Método que aplica gravedad en los objetos móviles
	 protected void applyGravity() {
		 Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
	
	    // Si se sale del tablero-->objeto muere
	    if (!pos.isValidPosition()) {
	        this.kill();
	        return;
	    }
	
	    // Si no hay suelo cae
	    if (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
	        pos = debajo;
	        isFalling = true;
	    } 
	    else {
	        isFalling = false;
	    }
	 }
	
	
	 //Método que controla el movimiento continuo de los objetos
	 protected void moveHorizontal() {
	    if (this.dir == 0) return; // STOP
	    Position newPos = pos.move(0, this.dir);
	
	    if (canMoveTo(newPos)) {
	        pos = newPos;
	    } 
	    else {
	        //Si choca cambia de dirección
	        this.dir = -this.dir;
	    }
	 }

	 //Método que sobrescribirá goomba y mario
	@Override
	public abstract void update();
	
}


