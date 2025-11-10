package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class GameObject implements GameItem { 

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
    protected void setPosition(Position newPos){
        this.pos = newPos;
    }
	//implement and decide, Which one is abstract?
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
    @Override
    public boolean interactWith(GameItem other) {
        boolean canInteract = other.isInPosition(this.pos);
        if (canInteract) {
         return other.receiveInteraction(this);  // Double Dispatch
     }
        return false;
    }

// implementaciones por defecto (cada subclase las personaliza)
    @Override
    public boolean receiveInteraction(Land obj) { return false; }

    @Override  
    public boolean receiveInteraction(ExitDoor obj) { return false; }

    @Override
    public boolean receiveInteraction(Mario obj) { return false; }

    @Override
    public boolean receiveInteraction(Goomba obj) { return false; }
}