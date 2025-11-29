package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.exceptions.GameLoadException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class LoadCommand extends AbstractCommand {
	private static final String NAME = Messages.COMMAND_LOAD_NAME;
	private static final String SHORTCUT = Messages.COMMAND_LOAD_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_LOAD_DETAILS;
	private static final String HELP = Messages.COMMAND_LOAD_HELP;
	
	private String fileName;
	
	public LoadCommand() {
		this(null);
    }

    private LoadCommand(String fileName) {
    		super(NAME, SHORTCUT, DETAILS, HELP);
    		this.fileName = fileName;
    }
    
    @Override
    public Command parse(String[] words) throws CommandParseException {
    	if (!matchCommandName(words[0])) {
    		return null;
        }

        if (words.length != 2) {
        	
        	throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        return new LoadCommand(words[1]); //creamos un loadcommand con el nombre del archivo
    }
    
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
    		try {
    		 //cargamos el archivo y mostramos el tablero
    			game.load(fileName);
    			view.showGame();
         }
    		catch (GameLoadException e) {
    			throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
         }
    }
    
}
