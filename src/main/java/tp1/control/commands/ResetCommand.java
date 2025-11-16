package tp1.control.commands;

import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

public class ResetCommand extends AbstractCommand {

    private static final String NAME     = Messages.COMMAND_RESET_NAME;
    private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
    private static final String DETAILS  = Messages.COMMAND_RESET_DETAILS;
    private static final String HELP     = Messages.COMMAND_RESET_HELP;

    private Integer level;  // null -> sin parámetro, número -> con parámetro

    // Constructor sin nivel (reset normalillo)
    public ResetCommand() {
        this(null);
    }

    // Constructor con bivel
    private ResetCommand(Integer level) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = level;
    }

    @Override
    public Command parse(String[] words) {
        // No coincide el comando
        if (!matchCommand(words[0])) return null;

        // reset sin parametros
        if (words.length == 1) {
            return new ResetCommand();
        }

        // reset con parámetro
        if (words.length == 2) {
            try {
                int lvl = Integer.parseInt(words[1]);
                return new ResetCommand(lvl);
            } catch (NumberFormatException e) {
                return null; // parámetro no válido
            }
        }

        // + de 2 --> no es para este comando
        return null;
    }

    @Override
    public void execute(GameModel game, GameView view) {
        if (level == null) {
            game.reset();//reset nivel actual
        } else {
            game.reset(level); //reset nivel que el usuario haya indicado
        }

        view.showGame();//tenemos que mostrar el tablero despues del reset
    }
}
