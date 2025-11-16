package tp1.control.commands;

import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 * Comando para mostrar la ayuda del juego.
 * Muestra todos los comandos disponibles y sus descripciones.
 */
public class HelpCommand extends NoParamsCommand {

    private static final String NAME = Messages.COMMAND_HELP_NAME;
    private static final String SHORTCUT = Messages.COMMAND_HELP_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_HELP_DETAILS;
    private static final String HELP = Messages.COMMAND_HELP_HELP;

    public HelpCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    @Override
    public void execute(GameModel game, GameView view) {
        // Obtener la ayuda de todos los comandos disponibles
        String helpText = CommandGenerator.commandHelp();
        // Mostrar la ayuda en la vista
        view.showMessage(helpText);
    }

}
