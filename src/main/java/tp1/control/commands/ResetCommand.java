package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 * Comando para reiniciar el juego.
 * Puede reiniciar el nivel actual o un nivel específico.
 * Uso: "reset" o "reset <nivel>"
 */
public class ResetCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_RESET_NAME;
    private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_RESET_DETAILS;
    private static final String HELP = Messages.COMMAND_RESET_HELP;

    private Integer level;

    public ResetCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = null; // Valor por defecto, resetea al nivel actual
    }

    public ResetCommand(int level) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = level;
    }

    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        
        // IMPORTANTE: Verificar que la primera palabra sea "reset" o "r"
        if (!matchCommandName(commandWords[0])) {
            return null;
        }
         // Si solo es "reset", devolver instancia sin nivel
        if (commandWords.length == 1) {
            return new ResetCommand();
        }
        // Si tiene 2 palabras, parsear el nivel
        if(commandWords.length == 2){
            try {
                int level = Integer.parseInt(commandWords[1]);
                return new ResetCommand(level);
            } catch (NumberFormatException nfe) {
                // Envolver la excepción
            throw new CommandParseException(
                Messages.LEVEL_NOT_A_NUMBER_ERROR.formatted(commandWords[1]), 
                nfe
            );
        }
    }
    // Si tiene más de 2 parámetros, error
    throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
}

    @Override
    public void execute(GameModel game, GameView view) {
        if(level == null) {
            game.reset(); // Resetea al nivel actual
        } else {
            game.reset(level); // Resetea al nivel especificado
        }
        // Mostrar el juego después del reset
        view.showGame();
    }
}
