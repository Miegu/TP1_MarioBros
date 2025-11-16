package tp1.control.commands;

import java.util.Arrays;
import tp1.logic.GameModel;
import tp1.logic.GameWorld;
import tp1.logic.GameObjectContainer;
import tp1.logic.gameobjects.GameObjectNew;
import tp1.logic.Position;
import tp1.logic.gameobjects.GameObjectFactory;
import tp1.view.GameView;
import tp1.view.Messages;

public class AddObjectCommand extends AbstractCommand {
	
	private static final String NAME = Messages.COMMAND_ADD_OBJECT_NAME;
	private static final String SHORTCUT = Messages.COMMAND_ADD_OBJECT_SHORTCUT;
	private static final String DETAILS = Messages.COMMAND_ADD_OBJECT_DETAILS;
	private static final String HELP = Messages.COMMAND_ADD_OBJECT_HELP;
	
	private String objDescription[];
	
	public AddObjectCommand() {
		super(NAME, SHORTCUT, DETAILS, HELP);
	}

	@Override
	public Command parse(String[] commandWords) {
		if(matchCommand(commandWords[0])) {	
			this.objDescription = Arrays.copyOfRange(commandWords, 1, commandWords.length);
			return this;
		}
		return null;
	}
	
	@Override
	public void execute(GameModel game, GameView view) {
		String objDescr = String.join(" ", objDescription);
				GameObjectNew gameObj = GameObjectFactory.parse(objDescription, (GameWorld) game);
		
		if(gameObj != null) {
			Position pos = gameObj.getPosition();
			 if(pos.isValidPosition()) {
			        game.addGameObject(gameObj);			view.showGame();
			 } else {
				 //view.showError()); --> que mensaje hay q poner ahi?		 
			 }
		} else {
			//view.showError();--> que mensaje hay q poner ahi
		}
	}

}
