package tp1.control.commands;

import java.util.Arrays;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.GameModel;
import tp1.logic.GameWorld;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Comando para añadir objetos del juego al tablero.
 *
 * Responsabilidades: 
 * - Parsear la descripción del objeto con su posición 
 * - Crear una instancia del objeto usando GameObjectFactory 
 * - Validar que la posición esté dentro del tablero 
 * - Añadir el objeto al mundo del juego 
 * - Actualizar la vista
 *
 * Formato: "addObject <posición> <tipo_objeto> [parámetros]" Ejemplo:
 * "addObject 2 3 goomba" (añade un goomba en posición 2,3)
 *
 * Solo puede ejecutarse si el modelo es una instancia de GameWorld, que
 * proporciona acceso directo al contenedor de objetos.
 */
public class AddObjectCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_ADDOBJECT_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ADDOBJECT_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ADDOBJECT_DETAILS;
    private static final String HELP = Messages.COMMAND_ADDOBJECT_HELP;

    /**
     * Descripción del objeto parseada por el usuario.
     */
    private String[] objectDescription;

    /**
     * Constructor por defecto sin descripción de objeto. Utilizado solo por el
     * patrón factory durante el parsing.
     */
    public AddObjectCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.objectDescription = null;
    }

    /**
     * Constructor privado con descripción del objeto. Se utiliza internamente
     * cuando el parsing tiene éxito.
     *
     * @param objectDescription Array con la descripción del objeto (posición +
     * tipo + parámetros)
     */
    protected AddObjectCommand(String[] objectDescription) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.objectDescription = objectDescription;
    }

    /**
     * Parsea la entrada del usuario para extraer la descripción del objeto.
     *
     * Algoritmo: 
     * 1. Comprueba si la primera palabra coincide con el comando 
     * 2. Valida que haya al menos 3 elementos (comando + posición + tipo) 
     * 3. Extrae el resto de la entrada como descripción del objeto
     *
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Nuevo AddObjectCommand con la descripción parseada
     * @throws CommandParseException Si la sintaxis es incorrecta
     */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        // Verificar que sea "addObject" o "aO"
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }
        // Comprobar que hay al menos 3 palabras (comando + posicion + descripción)
        if (commandWords.length < 3) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        // Copiar el resto del comando como descripción del objeto
        // Esto incluye posición, tipo de objeto y cualquier parámetro adicional
        String[] objDesc = Arrays.copyOfRange(commandWords, 1, commandWords.length);
        return new AddObjectCommand(objDesc);
    }

    /**
     * Ejecuta el comando para añadir un objeto.
     *
     * Pasos: 
     * 1. Castea el modelo a GameWorld (acceso directo) 
     * 2. Parsea la descripción del objeto usando GameObjectFactory 
     * 3. Valida que la posición esté dentro del tablero 
     * 4. Añade el objeto al mundo 
     * 5. Actualiza la vista
     *
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si ocurre un error durante la ejecución
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        try {
            GameWorld gameWorld = (GameWorld) game;

            // Parsear la descripción del objeto usando el factory
            GameObject obj = GameObjectFactory.parse(objectDescription, gameWorld);

            // Validar que la posición del objeto esté dentro del tablero
            if (!gameWorld.isInside(obj.getPosition())) {
                // Crear excepción con detalles de la descripción del objeto
                ObjectParseException cause = new ObjectParseException(
                        Messages.ERROR_OBJECT_POSITION_OFF_BOARD.formatted(String.join(" ", objectDescription))
                );
                throw new CommandExecuteException(
                        Messages.ERROR_COMMAND_EXECUTE, cause);
            }

            // Añadir el objeto al mundo del juego
            gameWorld.addObject(obj);

            // Mostrar el estado actualizado en la vista
            view.showGame();
        } catch (ObjectParseException e) {
            // Error al parsear la descripción del objeto
            throw new CommandExecuteException(
                    Messages.ERROR_COMMAND_EXECUTE, e);
        } catch (OffBoardException e) {
            // Error si la posición está fuera del tablero
            throw new CommandExecuteException(
                    Messages.ERROR_COMMAND_EXECUTE, e);
        }

    }
}
