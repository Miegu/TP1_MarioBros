package tp1.control.commands;

import java.util.List;
import java.util.ArrayList;

import tp1.view.Messages;
import tp1.logic.Action;
import tp1.logic.GameModel;
import tp1.view.GameView;
/* Comando para procesar las acciones del jugador */

public class ActionCommand extends AbstractCommand {
    
    private static final String NAME = Messages.COMMAND_ACTION_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ACTION_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ACTION_DETAILS;
    private static final String HELP = Messages.COMMAND_ACTION_HELP;

    private List<String> actions;

    public ActionCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = new ArrayList<>();
    }

    private ActionCommand(List<String> actions) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.actions = actions;
    }

    @Override
    public void execute(GameModel game, GameView view){
        //Agregar las acciones a la logica del juego
        for(Action action : actions){
            game.addAction(action);
        }

        //Actualiza el estado del juego
        game.update();
        //Muestra el estado actualizado
        view.showGame();
    }
    @Override
    public Command parse(String[] commandWords){
        //Necesitamos al menos el comando y una accion
        if (commandWords.length < 2 || commandWords == null) {
            return null;
        }
        //Comprueba si la primera palabra coincide con el comando
        if (!matchCommandName(commandWords[0])) {
            return null;
        }

        //Parsea las acciones
        List<Action> parsedActions = new ArrayList<>();
        for(int i = 1; i < commandWords.length; i++){
            Action action = Action.parse(commandWords[i]);
            if(action == null){
                System.out.println(Messages.ERROR.formatted(Messages.UNKNOWN_ACTION.formatted(commandWords[i])));
                return null;
            }
            parsedActions.add(action);
        }

        //Devuelve un nuevo comando con las acciones parseadas
        return new ActionCommand(parsedActions);
    }
}
