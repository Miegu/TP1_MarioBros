package tp1.control.commands;

import tp1.logic.GameModel;
import tp1.view.GameView;
/*  */
public interface Command {

	public void execute(GameModel game, GameView view);
	// Parsea la entrada del usuario y devuelve el comando si coincide
	public Command parse(String[] commandWords);
	// Devuelve el texto de ayuda del comando
	public String helpText();
}
