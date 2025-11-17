package tp1.control.commands;

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
    public Command parse(String[] commandWords) {
        
        if (commandWords.length < 1 || commandWords.length > 2) {
            return null;
        }
        // IMPORTANTE: Verificar que la primera palabra sea "reset" o "r"
        if (!matchCommandName(commandWords[0])) {
            return null;
        }
         // Si solo es "reset", devolver instancia sin nivel
        if (commandWords.length == 1) {
            return new ResetCommand();
        }
        // Si tiene 2 palabras, parsear el nivel
        try {
            int level = Integer.parseInt(commandWords[1]);
                return new ResetCommand(level);
        } catch (NumberFormatException e) {
            return null; // Parámetro inválido
        }
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
