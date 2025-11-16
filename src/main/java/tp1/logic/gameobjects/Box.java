package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.GameItem;
import tp1.logic.Position;
import tp1.view.Messages;

public class Box extends GameObjectNew {

    private static final int POINTS = 50;
    private boolean full;  //true llena y false vacía

    // Constructor
    public Box(GameWorld game, Position pos, boolean full) {
        super(game, pos);
        this.full = full;
    }
    
    public boolean isFull() {
    	return this.full;
    }
    
    public void changeBox(boolean state) { //state=true (si llena)/false(si vacia)
    	this.full=state;
    }
    
    @Override
    public String getIcon() {
        return full ? Messages.BOX : Messages.EMPTY_BOX;
    }

    @Override
    public void update() {
        //no hace nada por sí sola
    }
    
    @Override
    public boolean isSolid() {
        //se comporta como land
        return true;
    }
    

    //solo recibe interacciones no las inicia
    @Override
    public boolean interactWith(GameItem other) {
    	//miramos si algun objeto está debajo golpeandolo
    	Position below = new Position(pos.getRow() + 1, pos.getCol());
	    boolean canInteract = other.isInPosition(below);
	
	    if (!canInteract) {
	        return false;
	    }
	    return other.receiveInteraction(this);    
    }
    
    @Override
    public boolean receiveInteraction(Mushroom obj) {
        return false;
    }

  
}
