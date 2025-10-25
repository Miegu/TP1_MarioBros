package tp1.logic.gameobjects;

<<<<<<< HEAD:src/tp1/logic/gameobjects/GameObject.java
import tp1.logic.Action;
import tp1.logic.Game;
import tp1.logic.Position;

public abstract class GameObject { // TODO 

	protected Position pos; // If you can, make it private.
	private boolean isAlive;
	protected Game game; 
	
	public GameObject(Game game, Position pos) {
		this.isAlive = true;
		this.pos = pos;
		this.game = game;
	}
	
	public boolean isInPosition(Position p) {
		// TODO fill your code here, it should depends on the status of the object
		return false;
	}
 	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void dead(){
		this.isAlive = false;
	}
	
	// TODO implement and decide, Which one is abstract?
	// public boolean isSolid()
	// public void update()
	
	public abstract String getIcon();

	// Not mandatory but recommended
	protected void move(Action dir) {
		// TODO Auto-generated method stub
	}
=======
import tp1.logic.Position;

/**
 *
 */
public abstract class GameObject {

    private Position pos;

    //Constructor base para todos los objetos del juego
    protected GameObject(Position pos) {
        this.pos = pos;
    }

    //Obtiene la posicion actual del objeto
    public Position getPosition() {
        return pos;
    }

    //Cambia la posicion del objeto
    public void setPosition(Position position) {
        this.pos = position;
    }

    //Metodo abstracto que sera implementado por las subclases
    public abstract String getIcon();

    public void update() {
        // Default implementation (can be overridden by subclasses)
    }

    //Comprueba si el objeto esta en una posicion concreta
    public boolean isInPosition(Position position) {
        return this.pos.equals(position);
    }

    public boolean estaVivo() {
        return true; // Por defecto, los objetos estan vivos
    }

    // Representacion en String del objeto
    @Override
    public String toString() {
        return getClass().getSimpleName() + " en " + pos.toString();
    }
>>>>>>> P1-grupo-C-DGADE:src/main/java/tp1/logic/gameobjects/GameObject.java
}
