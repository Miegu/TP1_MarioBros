package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    public Land(GameWorld game, Position pos) {
        super(game, pos);
    }
    protected Land(){
        super();
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

}
