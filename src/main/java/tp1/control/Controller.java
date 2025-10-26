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
		
		
		
		//Parte de commands-> para usar patron Command
		while (!game.isFinished()) {

		    String[] userWords = view.getPrompt();
		    Command command = CommandGenerator.parse(userWords);

		    if (command != null) 
				command.execute(game, view);
		    else 
		        view.showError(Messages.UNKNOWN_COMMAND.formatted(String.join(" ", words)));
		}   
	}
	
	
	
	
	
	