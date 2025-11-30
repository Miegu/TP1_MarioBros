package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.exceptions.ObjectParseException;
import tp1.logic.GameWorld;
import tp1.view.Messages;
/**
 * Factoría de objetos del juego.
 * Parsea descripciones de objetos y crea instancias correspondientes.
 */
public class GameObjectFactory {

    // Lista de objetos disponibles
    private static final List<GameObject> availableObjects = Arrays.asList(
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
    public static GameObject parse(String[] objWords, GameWorld game)throws ObjectParseException { {
        if (objWords == null || objWords.length == 0) {
            throw new ObjectParseException(Messages.INVALID_GAME_OBJECT);
            }
        // Intentar parsear con cada prototipo de objeto
        //si el tipo no existe en vez de lanzar null lanza objectparse exception
        //si la pos esta fuera en vez de devolver null lnza offboardexception
        for (GameObject prototype : availableObjects) {
            GameObject obj = prototype.parse(objWords, game);
            if (obj != null) {
                return obj;
            }
        }
        // Si ningún tipo reconoce la descripcion-->lanzar excepcion
        String objString = String.join(" ", objWords);
        throw new ObjectParseException(Messages.INVALID_GAME_OBJECT.formatted(objString));
    }
    }   
}
