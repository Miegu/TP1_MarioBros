package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Comando para actualizar el estado del juego sin realizar ninguna acción especial.
 * 
 * Responsabilidades:
 * - Avanzar el juego un ciclo/turno
 * - Procesar actualización sin acciones de Mario
 * - Mostrar el nuevo estado del tablero
 * 
 * Formato: "update" o "u" o simplemente presionar ENTER
 * 
 * Este comando permite que los objetos del juego (enemigos, trampas, etc.)
 * avancen un turno sin que Mario realice ninguna acción.
 * 
 * Es equivalente a "ActionCommand sin acciones", pero más eficiente.
 */
public class UpdateCommand extends NoParamsCommand {

    private static final String NAME = Messages.COMMAND_UPDATE_NAME;
    private static final String SHORTCUT = Messages.COMMAND_UPDATE_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_UPDATE_DETAILS;
    private static final String HELP = Messages.COMMAND_UPDATE_HELP;

    /**
     * Constructor para inicializar el comando de actualización.
     */
    public UpdateCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    /**
     * Ejecuta el comando de actualización.
     * 
     * Pasos:
     * 1. Actualiza el estado del juego (un ciclo/turno) sin acciones de Mario
     * 2. Muestra el nuevo estado del tablero
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     */
    @Override
    public void execute(GameModel game, GameView view) {
        // Actualiza el estado del juego (un ciclo/turno)
        game.update();

        // Muestra el nuevo estado del tablero
        view.showGame();
    }

    /**
     * Parsea la entrada del usuario para reconocer el comando de actualización.
     * 
     * Casos aceptados:
     * 1. Entrada vacía (array de longitud 0) - presionar ENTER
     * 2. String vacío como única palabra
     * 3. "update" o "u" como única palabra
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return UpdateCommand si coincide, null si no
     * @throws CommandParseException Si es este comando pero con parámetros extra
     */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        // Aceptar entrada vacía (solo Enter)
        if (commandWords.length == 0) {
            return new UpdateCommand();
        }

        // Aceptar un string vacío
        if (commandWords.length == 1 && commandWords[0].isEmpty()) {
            return new UpdateCommand();
        }

        // Verificar si es "update" o "u"
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }
        
        // Si coincide pero hay parámetros extra, error
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        return this;
    }
}
