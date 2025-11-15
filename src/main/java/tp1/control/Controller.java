package tp1.control;
import java.util.Scanner;

import tp1.logic.Action;
import tp1.logic.GameModel;
import tp1.control.commands.*;
import tp1.view.GameView;
import tp1.view.Messages;

public class Controller {

	private GameModel game;
	private GameView view;

	public Controller(GameModel game, GameView view) {
		this.game = game;
		this.view = view;
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
	
	
	
	
	
	