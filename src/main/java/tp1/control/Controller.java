package tp1.control;
//hola
import java.util.Scanner;

import tp1.logic.Action;
import tp1.logic.Game;
import tp1.control.commands.*;
import tp1.view.GameView;
import tp1.view.Messages;

public class Controller {

	private Game game;
	private GameView view;

	public Controller(Game game, GameView view) {
		this.game = game;
		this.view = view;
		this.scanner = new Scanner(System.in);
	}


	public void run() {
		view.showWelcome();
		view.showGame();
		
		while (!game.isFinished()) {

		    String[] userWords = view.getPrompt(); //pedimos a la vista q lea lo q ha escrito el usuario
		    Command command = CommandGenerator.parse(userWords);

		    if (command != null) 
				command.execute(game, view);
		    else 
		        view.showError(Messages.UNKNOWN_COMMAND.formatted(String.join(" ", userWords)));
		}   
	}
	
	}
	
	
	
	
	
	