package tp1.control;

import java.util.Scanner;

import tp1.logic.Action;
import tp1.logic.Game;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private Game game;
	private GameView view;
	private Scanner scanner;

	public Controller(Game game, GameView view) {
		this.game = game;
		this.view = view;
		this.scanner = new Scanner(System.in);
	}


	/**
	 * Runs the game logic, coordinate Model(game) and View(view)
	 * 
	 */
	public void run() {
		view.showWelcome();

		boolean exit = false;
		
		//TODO fill your code: The main loop that displays the game, asks the user for input, and executes the action.

		while(!exit && !game.playerWins() && !game.playerLoses()){
   			 view.showGame();
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
			return false;
		}

		String[] parts = accion.split("\\s+");
		String command = parts[0].toLowerCase();

		switch(command){
			case "help", "h" -> {
				showHelp();
				return false;
			}
			
			case "exit", "e" -> {
                 return true;
			}
				
			case "reset", "r" -> {
				handleReset(parts);
				return false;
			}
			case "update","u" -> {
				game.update();
				return false;
                }

			case "action","a" -> {
				return handleActions(parts);
			}
                
			
			default -> {
				System.out.println(Messages.ERROR.formatted(Messages.UNKNOWN_COMMAND.formatted(command)));
				return false;
			}
		}
	}
	
	private boolean handleActions(String[] parts){
		if(parts.length < 2){
			System.out.println(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER));
			return false;
		}

		boolean hasValidActions = false;
		for(int i = 1; i < parts.length; i++){
			Action action = Action.parse(parts[i]);
			if(action == null){
				System.out.print(Messages.ERROR.formatted(Messages.INVALID_COMMAND_PARAMETERS));
			}else{
				game.addAction(action);
				hasValidActions = true;
			}
		}
		if(hasValidActions){
			game.update();
		}

		return false;
	}
	
	//Muestra la ayuda
	private void showHelp() {
		for (String line : Messages.HELP_LINES) {
			System.out.println(line);
		}
	}

	private void handleReset(String[] parts) {
        if (parts.length > 1) {
            // Hay un nivel especificado
            try {
                int level = Integer.parseInt(parts[1]);
                game.reset(level);
            } catch (NumberFormatException e) {
                System.out.println(Messages.ERROR.formatted(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER.formatted(parts[1])));
            }
        } else {
            // Reset sin nivel especificado
            game.reset();
        }
    }
}

