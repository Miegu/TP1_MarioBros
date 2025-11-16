package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.GameItem;
import tp1.logic.Position;

public abstract class GameObjectNew implements GameItem{
	
	//Atributos
	protected Position pos;
	protected GameWorld game;
	protected boolean alive;
	
	
	//Constructor
	protected GameObjectNew(GameWorld game, Position pos) {
		this.game = game;
        this.pos = pos;
        this.alive = true;
    }
	
	
	//Otros métodos:
	//Obtiene la posicion actual del objeto
    public Position getPosition() {
        return pos;
    }

    //Cambia la posicion del objeto
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    //Indica si nuestro objeto está vivo
    public boolean isAlive() {
        return alive;
    }
    
    //Matamos a nuestro objeto
    public void kill() {
        alive = false;
    }
    
    @Override
    public boolean isInPosition(Position position) {
        return this.pos.equals(position);
    }
    
    @Override
    public boolean isSolid() {
        return false;  //para los objetos sólidos sobreescribiremos el método
    }
    
    //Metodos abstractos que seran implementado por las subclases:
    public abstract String getIcon();
    public abstract void update();
    
    //metodos de interacción para cada objeto -->por ahora todo a false
    @Override
    public abstract boolean interactWith(GameItem other);
    
    @Override
    public boolean receiveInteraction(Land obj)     { 
    	return false; 
    	}
    @Override
    public boolean receiveInteraction(ExitDoor obj) { 
    	return false; 
    	}
    @Override
    public boolean receiveInteraction(Mario obj)    { 
    	return false; 
    	}
    @Override
    public boolean receiveInteraction(Goomba obj){
    	return false; 
    }
    @Override
    public boolean receiveInteraction(Box obj){
    	return false; 
    }
    @Override
    public boolean receiveInteraction(Mushroom obj) {
        return false;
    }

    
}