package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

    public ExitDoor(Position pos) {
        super(pos);
    }

    protected ExitDoor(){
        super();
    }

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

}
