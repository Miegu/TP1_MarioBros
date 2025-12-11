package tp1.control.commands;

import java.util.ArrayList;
import java.util.List;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.Action;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Comando para procesar las acciones de Mario.
 * 
 * Responsabilidades:
 * - Parsear una secuencia de acciones introducidas por el usuario
 * - Validar que las acciones sean válidas
 * - Ejecutar las acciones sobre Mario en el modelo
 * - Actualizar la vista con el nuevo estado
 * 
 * Formato: "a <acción1> [acción2] [acción3] ..."
 * Ejemplo: "a r r u" (derecha, derecha, saltar)
 * 
 * Las acciones se procesan todas en un mismo turno (ciclo) del juego.
 */

public class ActionCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_ACTION_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ACTION_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ACTION_DETAILS;
    private static final String HELP = Messages.COMMAND_ACTION_HELP;

    /** Lista de acciones a ejecutar. */
    private List<Action> actions;

    /**
     * Constructor por defecto sin acciones.
     * Utilizado solo por el patrón factory durante el parsing.
     */
    public ActionCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = new ArrayList<>();
    }

    /**
     * Constructor privado con lista de acciones específicas.
     * Se utiliza internamente cuando el parsing tiene éxito.
     * 
     * @param actions Lista de acciones parseadas del usuario
     */
    private ActionCommand(List<Action> actions) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = actions;
    }

    /**
     * Ejecuta el comando de acción.
     * 
     * Pasos:
     * 1. Valida que la lista de acciones no esté vacía
     * 2. Añade todas las acciones a Mario
     * 3. Actualiza el estado del juego (un ciclo/turno)
     * 4. Muestra el estado actualizado en la vista
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si ocurre un error durante la ejecución
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        //Valida que la lista de acciones no esté vacía
        if (actions == null || actions.isEmpty()) {
            throw new CommandExecuteException(
                    Messages.ERROR_INCORRECT_ACTION_EMPTY_LIST
            );
        }
        //Añade todas las acciones a Mario para este turno
        for (Action action : actions) {
            game.addAction(action);
        }

        //Actualiza el estado del juego
        game.update();

        //Muestra el estado actualizado
        view.showGame();
    }

    /**
     * Parsea la entrada del usuario para extraer acciones.
     * 
     * Algoritmo:
     * 1. Comprueba si la primera palabra coincide con el comando
     * 2. Valida que haya al menos una acción después del comando
     * 3. Parsea cada palabra como una acción individual
     * 4. Recopila acciones válidas y registra las inválidas
     * 5. Valida que al menos una acción sea válida
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Nuevo ActionCommand con las acciones parseadas
     * @throws CommandParseException Si la sintaxis es incorrecta o hay errores de validación
     */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        //Comprueba si la primera palabra coincide con el comando
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }
        //Necesitamos al menos el comando y una accion
        if (commandWords.length < 2) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        // Parsea las acciones
        List<Action> parsedActions = new ArrayList<>();
        List<String> failedActions = new ArrayList<>();

        // Procesar cada palabra como una acción (ignorando la primera que es el comando)
        for (int i = 1; i < commandWords.length; i++) {
            try {
                // Intentar parsear la palabra como una acción
                Action action = Action.parse(commandWords[i]);
                parsedActions.add(action);
            } catch (ActionParseException ape) {
                // Registrar las acciones que no se pudieron parsear
                failedActions.add(commandWords[i]);
            }
        }

        // Validar que al menos una acción se haya parseado correctamente
        if (parsedActions.isEmpty()) {
            throw new CommandParseException(
                    Messages.ERROR_INCORRECT_ACTION_EMPTY_LIST
            );
        }

        //Devuelve un nuevo comando con las acciones parseadas
        return new ActionCommand(parsedActions);
    }
}
