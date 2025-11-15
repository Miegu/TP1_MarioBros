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
    	for (GameObjectNew o : gameObjects) {
        	   o.update();  
           }
   
    	//Elimianmos los muertos
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
    
    //Devolvemos el 1º objeto que este en es pos -->para interacciones -->hace falta?
    public GameObjectNew getObjectIn(Position pos) {
        for (GameObjectNew o : gameObjects) {
            if (o.isInPosition(pos)) {
                return o;
            }
        }
        return null;
    }
}
