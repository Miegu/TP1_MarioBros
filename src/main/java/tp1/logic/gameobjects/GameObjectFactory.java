package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.logic.GameWorld;
/**
 * Factoría de objetos del juego.
 * Parsea descripciones de objetos y crea instancias correspondientes.
 */
public class GameObjectFactory {

    // Lista de objetos disponibles
    private static final List<GameObject> availableObjects = Arrays.<GameObject>asList(
        new Land(),
        new ExitDoor(),
        new Goomba(),
        new Mario(),
        new Mushroom(),
        new Box()
    );
    /**
     * Parsea una descripción de objeto y devuelve la instancia correspondiente.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game Referencia al mundo del juego
     * @return GameObject parseado o null si no se reconoce
     */
    public static GameObject parse(String[] objWords, GameWorld game) {
        if (objWords == null || objWords.length == 0) {
            return null;
        }
        // Intentar parsear con cada prototipo de objeto
        for (GameObject prototype : availableObjects) {
            GameObject obj = prototype.parse(objWords, game);
            if (obj != null) {
                return obj;
            }
        }
        // Si ningún prototipo reconoce la descripción
        return null;
    }
}
