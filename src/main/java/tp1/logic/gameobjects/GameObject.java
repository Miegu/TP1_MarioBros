package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class GameObject { 

	protected Position pos; // If you can, make it private.
	private boolean isAlive;
	protected GameWorld game;
	
    //Constructor con GameWorld y Position
	public GameObject(GameWorld game, Position pos) {
		this.isAlive = true;
		this.pos = pos;
		this.game = game;
	}
	//Constructor Necesario para GameObjectFactory
    protected GameObject(){
        this.isAlive = true;
        this.pos = null;
        this.game = null;
    }

    public GameObject parse(String[] objWords, GameWorld game) {
    return null; // Implementación por defecto
    }

	public boolean isInPosition(Position p) {
		return this.pos != null && this.pos.equals(p);
	}
 	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void dead(){
		this.isAlive = false;
	}
	
    public Position getPosition(){
        return pos;
    }
	// TODO implement and decide, Which one is abstract?
    public abstract String getIcon();
	public abstract boolean isSolid();
    //Las subclases pueden sobreescribirlo
	 public void update(){

     }

	// Not mandatory but recommended
	protected void move(Action dir) {
        if(dir != null && pos != null){
            Position newPos = pos.move(dir);
            if(newPos != null && game != null && game.isInside(newPos)){
                this.pos = newPos;
            }
        }
	}
    // Representacion en String del objeto
    @Override
    public String toString() {
        return getClass().getSimpleName() + " en " + pos.toString();
    }
}

/**
 *public abstract class GameObject {

    

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

    
}
 */

