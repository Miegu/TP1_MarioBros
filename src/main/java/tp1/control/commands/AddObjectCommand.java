package tp1.control.commands;

import java.util.Arrays;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.GameModelException;
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
    public void execute(GameModel game, GameView view) throws CommandExecuteException{
        // 1. Intenta parsear el objeto usando GameObjectFactory
        GameObject obj = GameObjectFactory.parse(objectDescription, game);
        
        if (obj == null) {
            // Error: objeto no válido
            //Construye mensaje de error
            String objString = String.join(" ", objectDescription);
            throw new CommandExecuteException(String.format(Messages.INVALID_GAME_OBJECT, objString));
        }
        
        //2. Añadir el objeto al juego
        try {
            game.addObject(obj);
        } catch (OffBoardException e) {
            //Se envuelve para no perder informacion
            throw new CommandExecuteException(Messages.ERROR_ADD_OBJECT, e);
        }

        view.showGame();
    }
}