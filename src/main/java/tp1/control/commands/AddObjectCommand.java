package tp1.control.commands;

import java.util.Arrays;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.GameModel;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.view.GameView;
import tp1.view.Messages;

public class AddObjectCommand extends AbstractCommand {
    
    private static final String NAME = Messages.COMMAND_ADDOBJECT_NAME;
    private static final String SHORTCUT = Messages.COMMAND_ADDOBJECT_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_ADDOBJECT_DETAILS;
    private static final String HELP = Messages.COMMAND_ADDOBJECT_HELP;

    private String[] objectDescription;
    
    public AddObjectCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.objectDescription = null;
    }
    
    protected AddObjectCommand(String[] objectDescription) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.objectDescription = objectDescription;
    }
    
    @Override
    public Command parse(String[] commandWords) {
        if (commandWords.length < 2) {
            return null;
        }
        
         // Verificar que sea "addObject" o "aO"
        if (!matchCommandName(commandWords[0])) {
            return null;
        }
        
        // Copiar el resto del comando como descripción del objeto
        String[] objDesc = Arrays.copyOfRange(commandWords, 1, commandWords.length);
        return new AddObjectCommand(objDesc);
    }
    
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {

        try {
        	//tanto metodo parse como add pueden lanzar excepciones
        	//se las mansa a command executeexceocion y estas a command exception y esta la captura coontroler
            GameObject obj = GameObjectFactory.parse(objectDescription, game);
            game.addObject(obj);
            view.showGame();

        } catch (OffBoardException e) {
            throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);

        } catch (ObjectParseException e) {
            throw new CommandExecuteException(Messages.ERROR_COMMAND_EXECUTE, e);
        }
    }

}