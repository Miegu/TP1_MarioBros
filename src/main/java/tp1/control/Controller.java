package tp1.control;
//hola
import java.util.Scanner;

import tp1.logic.Action;
import tp1.logic.Game;
import tp1.view.GameView;
import tp1.view.Messages;

public class Controller {

	private Game game;
	private GameView view;
	private Scanner scanner;

	public Controller(Game game, GameView view) {
		this.game = game;
		this.view = view;
		this.scanner = new Scanner(System.in);
	}


	public void run() {
		view.showWelcome();
		view.showGame();
		boolean exit = false;
		
		//TODO fill your code: The main loop that displays the game, asks the user for input, and executes the action.

		while(!exit && !game.playerWins() && !game.playerLoses()){
    		String[] words = view.getPrompt();  
   			exit = processCommand(String.join(" ", words));
	}
		//Cuando pierde o gana
		if(game.playerWins()){
			System.out.println(Messages.MARIO_WINS);
		}else if(game.playerLoses()){
			System.out.println(Messages.GAME_OVER);
		}
	}
	/*
	 * Procesa la accion dada por el usuario
	 */
	private boolean processCommand(String accion) {
		if(accion.isEmpty()){
			//Si el comando esta vacio, se actualiza
			game.update();
			view.showGame();
			return false;
		}

		String[] parts = accion.split("\\s+");
		String command = parts[0].toLowerCase();

		switch(command){
			case "help", "h" -> {
				if(parts.length > 1) {
       				System.out.println(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER));
    			} else {
        			showHelp();
    		}
				return false;
			}
			
			case "exit", "e" -> {
				System.out.println(Messages.PLAYER_QUITS);
                 return true;
			}
				
			case "reset", "r" -> {
				handleReset(parts);
				return false;
			}
			case "update","u" -> {
				game.update();
				view.showGame();
				return false;
                }

			case "action","a" -> {
				return handleActions(parts);
			}
                
			
			default -> {
				System.out.println(Messages.ERROR.formatted(Messages.UNKNOWN_COMMAND.formatted(parts[0])));
				return false;
			}
		}
	}
	
	private boolean handleActions(String[] parts){
		if(parts.length < 2){
			System.out.println(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER));
			return false;
		}
		for(int i = 1; i < parts.length; i++){
			Action action = Action.parse(parts[i]);
			if(action == null){
				System.out.println(Messages.ERROR.formatted(Messages.UNKNOWN_ACTION.formatted(parts[i])));
			}else{
				game.addAction(action);
			}
		}
		
		game.update();
		view.showGame();

		return false;
	}
	
	//Muestra la ayuda
	private void showHelp() {
		System.out.println(Messages.HELP);
	}

	private void handleReset(String[] parts) {
        if (parts.length > 1) {
            // Hay un nivel especificado
            try {
                int level = Integer.parseInt(parts[1]);
                game.reset(level);
				view.showGame();
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER));
            }
        } else {
            // Reset sin nivel especificado
            game.reset();
			view.showGame();
        }
    }
}

