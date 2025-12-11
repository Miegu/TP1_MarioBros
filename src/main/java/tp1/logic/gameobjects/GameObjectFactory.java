package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.exceptions.ObjectParseException;
import tp1.logic.GameWorld;
import tp1.view.Messages;
/**
 * Factoría de objetos del juego usando el patrón Prototype.
 * 
 * Responsabilidades:
 *   Parsear descripciones de objetos desde strings
 *   Crear instancias de los objetos correspondientes
 *   Validar que el tipo de objeto sea reconocido
 * 
 * Patrón Prototype:
 *   Mantiene una lista de prototipos (instancias vacías de cada tipo)
 *   Cada prototipo intenta parsear la descripción
 *   El primero que reconoce el tipo devuelve una nueva instancia
 */
public class GameObjectFactory {

    /**
     * Lista de prototipos de todos los objetos disponibles en el juego.
     * Cada objeto debe tener un constructor sin argumentos para ser usado como prototipo.
     */
    private static final List<GameObject> availableObjects = Arrays.asList(
        // Objetos estáticos
        new Land(),
        new ExitDoor(),
        new Box(),
        
        // Objetos móviles
        new Goomba(),
        new Mushroom(),
        new Mario()
    );
    /**
     * Parsea una descripción de objeto y devuelve la instancia correspondiente.
     * Proceso:
     *   Valida que objWords no sea null o vacío
     *   Itera por cada prototipo de objeto
     *   Llama a parse() de cada prototipo
     *   El primero que devuelva no-null es el tipo correcto
     *   Si ninguno reconoce el tipo, lanza ObjectParseException
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game Referencia al mundo del juego
     * @return GameObject parseado o null si no se reconoce
     */
    public static GameObject parse(String[] objWords, GameWorld game)throws ObjectParseException {
         // Validar entrada
        if (objWords == null || objWords.length == 0) {
            throw new ObjectParseException(Messages.INVALID_GAME_OBJECT);
            }
        // Intentar parsear con cada prototipo de objeto
        //si el tipo no existe en vez de lanzar null lanza objectparse exception
        //si la pos esta fuera en vez de devolver null lnza offboardexception
        for (GameObject prototype : availableObjects) {
            GameObject obj = prototype.parse(objWords, game);
            if (obj != null) {
                return obj; // Tipo reconocido, devolver instancia
            }
        }
        // Si ningún tipo reconoce la descripcion-->lanzar excepcion
        String objString = String.join(" ", objWords);
        throw new ObjectParseException(Messages.INVALID_GAME_OBJECT.formatted(objString));
    }
}
