package tp1.control;
import tp1.exceptions.CommandExecuteException;
import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Acept
 */
public class Controller {

	//unica clase q puede hablar con el usuario-->le tienen que llegar todos las exceptions
    private GameModel game;
    private GameView view;

    public Controller(GameModel game, GameView view) {
        this.game = game;
        this.view = view;
    }

    
    public void run() {
        view.showWelcome();
        view.showGame();
        boolean exit = false;

        while (!game.isFinished()) {
            String[] words = view.getPrompt();

            try {
                Command command = CommandGenerator.parse(words);
                command.execute(game, view);
            } 
            //si falla algo intentando parsear o ejecutar->le llega a esta funcion
            catch (tp1.exceptions.CommandException e) {
            	//muestra el mensaje de error y TODAS Las causas internas
                view.showError(e.getMessage());
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