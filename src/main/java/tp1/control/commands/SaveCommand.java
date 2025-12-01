package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameModelException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class SaveCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_SAVE_NAME;
    private static final String SHORTCUT = Messages.COMMAND_SAVE_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_SAVE_DETAILS;
    private static final String HELP = Messages.COMMAND_SAVE_HELP;

    private String fileName;

    public SaveCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.fileName = null;
    }

    private SaveCommand(String fileName) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.fileName = fileName;
    }

   //si no coincide con el nombre no es error, se devuelve null
    @Override
    public Command parse(String[] words) throws CommandParseException {
        if (!matchCommandName(words[0])) {
            return null; // no es este comando
        }

        if (words.length != 2) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        return new SaveCommand(words[1]);
    }

    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        try {
            game.save(fileName);
        } 
        catch (GameModelException e) {
            throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
        }
    }
}