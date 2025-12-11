package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameLoadException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Comando para cargar un juego previamente guardado.
 * 
 * Responsabilidades:
 * - Parsear el nombre del archivo a cargar
 * - Validar que el archivo exista
 * - Cargar el estado completo del juego desde el archivo
 * - Actualizar la vista con el estado cargado
 * 
 * Formato: "load <nombre_archivo>" o "l <nombre_archivo>"
 * Ejemplo: "load partida1.sav" (carga desde fichero partida1.sav)
 * 
 * El archivo debe haber sido previamente guardado con SaveCommand.
 * Reemplaza completamente el estado actual del juego.
 * 
 * Nota: Requiere exactamente un parámetro (el nombre del archivo).
 */
public class LoadCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_LOAD_NAME;
    private static final String SHORTCUT = Messages.COMMAND_LOAD_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_LOAD_DETAILS;
    private static final String HELP = Messages.COMMAND_LOAD_HELP;

	//Nombre del archivo a cargar
    private String fileName;

	/**
     * Constructor por defecto sin nombre de archivo.
     * Utilizado solo internamente.
     */
    public LoadCommand() {
        this(null);
    }

	/**
     * Constructor privado con nombre de archivo.
     * Se utiliza internamente cuando el parsing tiene éxito.
     * 
     * @param fileName Nombre del archivo a cargar
     */
    private LoadCommand(String fileName) {
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
     * @return LoadCommand con el nombre de archivo parseado
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
        return new LoadCommand(words[1]); //creamos un loadcommand con el nombre del archivo
    }

	/**
     * Ejecuta el comando para cargar un juego guardado.
     * 
     * Pasos:
     * 1. Llama al método load() del modelo con el nombre del archivo
     * 2. Si la carga es exitosa:
     *    a. El estado del juego es completamente reemplazado
     *    b. La vista se actualiza para mostrar el nuevo estado
     * 3. Si ocurre un error:
     *    a. Lo envuelve en CommandExecuteException con mensaje específico
     *    b. El estado del juego anterior se mantiene intacto
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si ocurre un error al cargar
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        try {
            // Cargar el estado del juego desde el archivo especificado
            game.load(fileName);

			// Mostrar el estado cargado en la vista
            view.showGame();
        } catch (GameLoadException e) {
			// Envolver la excepción con mensaje específico
            throw new CommandExecuteException(Messages.ERROR_UNABLE_TO_LOAD.formatted(fileName), e);
        }
    }

}
