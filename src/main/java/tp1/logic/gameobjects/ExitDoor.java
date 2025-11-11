package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

    public ExitDoor(GameWorld game, Position pos) {
        super(game, pos);
    }

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
        // Mario ha llegado a la puerta de salida
        game.marioReachedExit();
        return true;
    }
    @Override
    public boolean canBeRemoved() {
        return false;
    }
    @Override
    public String toString() {
        return "ExitDoor at " + pos.toString();
    }
}
