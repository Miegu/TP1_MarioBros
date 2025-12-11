package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameModelException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Comando para guardar el estado actual del juego en un archivo.
 * 
 * Responsabilidades:
 * - Parsear el nombre del archivo donde guardar
 * - Validar que el nombre sea válido
 * - Guardar el estado completo del juego (tablero, objetos, posiciones)
 * - Informar al usuario del resultado
 * 
 * Formato: "save <nombre_archivo>" o "s <nombre_archivo>"
 * Ejemplo: "save partida1.sav" (guarda en fichero partida1.sav)
 * 
 * El archivo se guardará en el directorio del proyecto o en una carpeta
 * de guardados predefinida, según la implementación de GameModel.save().
 * 
 * Nota: Requiere exactamente un parámetro (el nombre del archivo).
 */
public class SaveCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_SAVE_NAME;
    private static final String SHORTCUT = Messages.COMMAND_SAVE_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_SAVE_DETAILS;
    private static final String HELP = Messages.COMMAND_SAVE_HELP;

    // Nombre del archivo donde guardar el juego
    private String fileName;

    /**
     * Constructor por defecto sin nombre de archivo.
     * Utilizado solo por el patrón factory durante el parsing.
     */
    public SaveCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.fileName = null;
    }

    /**
     * Constructor privado con nombre de archivo.
     * Se utiliza internamente cuando el parsing tiene éxito.
     * 
     * @param fileName Nombre del archivo donde guardar
     */
    private SaveCommand(String fileName) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.fileName = fileName;
    }

    /**
     * Parsea la entrada del usuario para extraer el nombre del archivo.
     * 
     * Requisitos:
     * - Exactamente 2 palabras (comando + nombre de archivo)
     * - Si no coincide el comando, devuelve null sin error
     * - Si coincide pero la cantidad de parámetros es incorrecta, lanza excepción
     * 
     * @param words Array de palabras introducidas por el usuario
     * @return SaveCommand con el nombre de archivo parseado
     * @throws CommandParseException Si la cantidad de parámetros es incorrecta
     */
    @Override
    public Command parse(String[] words) throws CommandParseException {
        // Si no coincide con el nombre, no es error, devolver null
        if (!matchCommandName(words[0])) {
            return null; // No es este comando, que otro lo intente
        }

        // Debe haber exactamente 2 palabras (comando + nombre archivo)
        if (words.length != 2) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        // Retorna una nueva instancia del comando con el nombre del archivo
        return new SaveCommand(words[1]);
    }

    /**
     * Ejecuta el comando para guardar el juego.
     * 
     * Pasos:
     * 1. Llama al método save() del modelo con el nombre del archivo
     * 2. Si ocurre un error, lo envuelve en CommandExecuteException
     * 3. No actualiza la vista (el estado del juego no cambió)
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si ocurre un error al guardar
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        try {
            // Guardar el estado del juego en el archivo especificado
            game.save(fileName);
        } catch (GameModelException e) {
            // Envolver la excepción en CommandExecuteException
            throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
        }
    }
}
