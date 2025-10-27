package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public class GameObjectFactory {
    
    private static final List<GameObject> availableObjects = Arrays.asList(
        new Land(),
        new ExitDoor(), 
        new Goomba(),
        new Mario()
        //new Box(),
        //new Mushroom()
    );
    
    public static GameObject parse(String[] objWords, GameWorld game) {
        //Verifica que hay al menos posicion y tipo de objeto
        if(objWords == null || objWords.length < 2){
            return null;
        }
        //Intenta parsear con cada objeto disponible
        for (GameObject obj : availableObjects) {
            GameObject parsed = obj.parse(objWords, game);
            if (parsed != null) {
                return parsed;
            }
        }
        return null; // No se pudo parsear
    }
}