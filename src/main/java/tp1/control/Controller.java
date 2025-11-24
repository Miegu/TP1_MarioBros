package tp1.control;

import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.exceptions.CommandException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Acept
 */
public class Controller {

    private GameModel game;
    private GameView view;

    public Controller(GameModel game, GameView view) {
        this.game = game;
        this.view = view;
    }

    /**
     * Runs the game logic, coordinate Model(game) and View(view)
     *
     */
    public void run() {
        view.showWelcome();
        view.showGame();

        while (!game.isFinished()) {
            String[] words = view.getPrompt();

            try {
                Command command = CommandGenerator.parse(words);
                command.execute(game, view);

            } catch (CommandException e) {
                // Muestra el mensaje principal del error
                view.showError(e.getMessage());

                // Muestra todos los mensajes encadenados (causas)
                Throwable cause = e.getCause();
                while (cause != null) {
                    view.showError(cause.getMessage());
                    cause = cause.getCause();
                }
            }
        }
    
        view.showEndMessage();
    }
}
