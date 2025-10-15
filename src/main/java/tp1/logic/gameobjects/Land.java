package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    public Land(Position pos) {
        super(pos);
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

}
