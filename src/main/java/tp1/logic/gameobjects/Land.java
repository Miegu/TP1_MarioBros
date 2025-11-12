package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    public Land(GameWorld game, Position pos) {
        super(game, pos);
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean canBeRemoved() {
        return false;
    }

    @Override
    public String toString() {
        return "Land at " + pos.toString();
    }
}
