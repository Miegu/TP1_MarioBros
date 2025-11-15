package tp1.logic;

import java.util.ArrayList;
import java.util.List;
import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.GameObjectNew;
import tp1.logic.Position;
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
    
    public boolean isSolid(Position pos) {
        for (GameObjectNew o : gameObjects) {
            if (o.isInPosition(pos) && o.isSolid()) {
                return true;
            }
        }
        return false;
    }


    public void update() {
    	//Actualizamos mario primero
    	for (GameObjectNew o : gameObjects) {
           if(o instanceof Mario) {
        	   o.update();  
           }
        }
    	
    	//Actualizamos el resto
    	for (GameObjectNew o : gameObjects) {
            if(!(o instanceof Mario)) {
         	   o.update();  
            }
    	}
    	
    	//Elimianmos los muertos
        gameObjects.removeIf(o -> !o.isAlive());
    }


}
