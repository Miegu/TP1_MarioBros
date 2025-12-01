package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 * Comando para actualizar el estado del juego sin realizar ninguna acción especial.
 */
public class UpdateCommand extends NoParamsCommand {

    private static final String NAME = Messages.COMMAND_UPDATE_NAME;
    private static final String SHORTCUT = Messages.COMMAND_UPDATE_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_UPDATE_DETAILS;
    private static final String HELP = Messages.COMMAND_UPDATE_HELP;

    public UpdateCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }
     /**
     * Ejecuta el comando de actualización.
     * Actualiza el estado del juego (un ciclo) y muestra el nuevo estado.
     */
    @Override
    public void execute(GameModel game, GameView view) {
        // Actualiza el estado del juego
        game.update();
        // Muestra el nuevo estado
        view.showGame();
    }
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
            return null;
        }
        // Si coincide pero hay parámetros extra, error
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        
        return this;
    }
}
