package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 * Comando para salir del juego.
 * Termina la partida de forma controlada.
 */
public class ExitCommand extends NoParamsCommand {

    // Forman parte de atributos de estado
    private static final String NAME = Messages.COMMAND_EXIT_NAME;
    private static final String SHORTCUT = Messages.COMMAND_EXIT_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_EXIT_DETAILS;
    private static final String HELP = Messages.COMMAND_EXIT_HELP;

    public ExitCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    @Override
    public void execute(GameModel game, GameView view) {
        // Marcar el juego como terminado
        game.exit();

        //El controller se encarga de mostrar el mensaje de exitgame
    }
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        if (!matchCommandName(commandWords[0])) {
            return null;
        }

        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        
        return new ExitCommand();
    }

}
