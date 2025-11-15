package tp1.logic;
import java.util.ArrayList;
import java.util.List;
import tp1.logic.gameobjects.GameObjectNew;
import tp1.logic.gameobjects.GameItem;
import tp1.view.Messages;

public class GameObjectContainer {
   

    private List<GameObjectNew> gameObjects;

    public GameObjectContainer() {
        gameObjects = new ArrayList<>();
    }

    //Método para añadir un objeto
    public void add(GameObjectNew o) {
    	gameObjects.add(o);
    }
    

    public void update() {
    	for (GameObjectNew o : gameObjects) {
        	   o.update();  
           }
   
    	//Eliminamos los muertos
        gameObjects.removeIf(o -> !o.isAlive());
    }

    
    //Devolvemos el icono de un det objeto en una pos
    public String getIconAt(Position pos) {
        for (GameObjectNew o : gameObjects) {
            if (o.isInPosition(pos)) {
                return o.getIcon();
            }
        }
        return Messages.EMPTY;
    }
    
    
    //Miramos si hay un objeto solido en una pos
    public boolean isSolid(Position pos) {
        for (GameObjectNew o : gameObjects) {
            if (o.isInPosition(pos) && o.isSolid()) {
                return true;
            }
        }
        return false;
    }


    //Manejamos las interacciones
    public void doInteraction(GameItem other) {
        for (GameObjectNew o : gameObjects) {
            if (o== other) continue; //no interactuar consigo mismo
            other.interactWith(o);
            o.interactWith(other);
        }
    }
}