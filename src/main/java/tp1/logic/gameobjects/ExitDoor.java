package tp1.logic.gameobjects;

import tp1.logic.Position;

public class ExitDoor extends GameObject {

    public ExitDoor(Position pos) {
        super(pos);
    }

    @Override
    public String getIcon() {
        return "🚪";
    }

}
