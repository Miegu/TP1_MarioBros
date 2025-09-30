package tp1.logic.gameobjects;

import tp1.logic.Position;

public class Land extends GameObject {

    public Land(Position pos) {
        super(pos);
    }

    @Override
    public char getIcon() {
        return 'L';
    }
    
}
