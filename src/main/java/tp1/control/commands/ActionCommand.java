package tp1.control.commands;

import java.util.Arrays;
import tp1.logic.Action;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class ActionCommand extends AbstractCommand {

	//ahí guardamos todas las palabras q vienen despues de action
    private String[] actions;

    //metodo para commandgenerator 
    public ActionCommand() {
        this(null);
    }

    //constructor que usamos cuando ya sabemos que acciones ha escrito el usuario
    //es prvate pq solo queremos q se use desde el parse
    private ActionCommand(String[] actions) {
        super(Messages.COMMAND_ACTION_NAME,
              Messages.COMMAND_ACTION_SHORTCUT,
              Messages.COMMAND_ACTION_DETAILS,
              Messages.COMMAND_ACTION_HELP);
        this.actions = actions;
    }

    @Override
    public Command parse(String[] words) {
        if (words.length < 2 || !matchCommand(words[0])) {
        	return null;
        }
        //copiamos desde la pos 1 hasta el final en actions
        String[] args = Arrays.copyOfRange(words, 1, words.length);
        return new ActionCommand(args);
    }

    @Override
    public void execute(GameModel game, GameView view) {
        if (actions == null || actions.length == 0) {
            view.showError(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER));
            return;
        }

        for (String a : actions) {
            Action action = Action.parse(a);
            if (action == null) {
                view.showError(Messages.ERROR.formatted(Messages.UNKNOWN_ACTION.formatted(a)));
            } else {
                game.addAction(action);
            }
        }

        game.update();
        view.showGame();
    }
}
