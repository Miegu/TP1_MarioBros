package tp1.logic.gameobjects;
import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObjectNew {
    
    public ExitDoor(Game game, Position pos) {
        super(game, pos);
    }

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

    @Override
    public void update() {
        //no se actualiza
    }
    
    @Override
    public boolean receiveInteraction(Mario mario) {
        game.marioExited();
        return true;
    }

    @Override
    public boolean interactWith(GameItem other) {
        return false;
    }

}