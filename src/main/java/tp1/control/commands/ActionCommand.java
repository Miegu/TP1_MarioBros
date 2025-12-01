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
/* Comando para procesar las acciones de Mario */

public class ActionCommand extends AbstractCommand {
    
    private static final String NAME = Messages.COMMAND_ACTION_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ACTION_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ACTION_DETAILS;
    private static final String HELP = Messages.COMMAND_ACTION_HELP;

    private List<Action> actions;

    /**
     * Constructor por defecto (sin acciones).
     */
    public ActionCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = new ArrayList<>();
    }
     /**
     * Constructor con lista de acciones específicas
     */
    private ActionCommand(List<Action> actions) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = actions;
    }

    /**
     * Ejecuta el comando de acción.
     * Añade todas las acciones a Mario y actualiza el juego.
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        //Valida que la lista de acciones no esté vacía
        if (actions == null || actions.isEmpty()) {
            throw new CommandExecuteException(
                Messages.ERROR_INCORRECT_ACTION_EMPTY_LIST
            );
        }
        //Añade todas las acciones a Mario
        for(Action action : actions){
            game.addAction(action);
        }

        //Actualiza el estado del juego
        game.update();
        //Muestra el estado actualizado
        view.showGame();
    }

    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        //Comprueba si la primera palabra coincide con el comando
        if (!matchCommandName(commandWords[0])) {
            return null;
        }
        //Necesitamos al menos el comando y una accion
        if (commandWords.length < 2) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        // Parsea las acciones
        List<Action> parsedActions = new ArrayList<>();
        List<String> failedActions = new ArrayList<>();

        for (int i = 1; i < commandWords.length; i++) {
            try {
                Action action = Action.parse(commandWords[i]);
                parsedActions.add(action);
            } catch (ActionParseException ape) {
                failedActions.add(commandWords[i]);
            }
        }
        
        if (parsedActions.isEmpty()) {
            throw new CommandParseException(
                Messages.ERROR_INCORRECT_ACTION_EMPTY_LIST
            );
        }

        //Devuelve un nuevo comando con las acciones parseadas
        return new ActionCommand(parsedActions);
    }
}
