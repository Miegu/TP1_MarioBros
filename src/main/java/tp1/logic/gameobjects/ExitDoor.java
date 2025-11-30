package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {

    public ExitDoor(GameWorld game, Position pos) {
        super(game, pos);
    }
    
    protected ExitDoor() {
        super();
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
    public boolean interactWith(GameItem other) {
        // La puerta permite que otros interactúen con ella
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
        // Mario ha llegado a la puerta de salida
        if (mario.isInPosition(this.getPosition())) {
            game.marioReachedExit();
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeRemoved() {
        return false;
    }

    @Override
    public String toString() {
        return "ExitDoor at " + getPosition().toString();
    }

    @Override
    public GameObject parse(String[] objWords, GameWorld game) {
        // Formato: (fila,col) EXITDOOR
        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("EXITDOOR") && !type.equals("ED")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0]);
        if (pos == null) return null;
        
        return new ExitDoor(game, pos);
    }

    private Position parsePosition(String posStr) {
        try {
        // Formato: (fila,columna)
        posStr = posStr.replace("(", "").replace(")", "");
        String[] parts = posStr.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        return new Position(row, col);
    } catch (Exception e) {
        return null;
        }
    }

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        return "(" + row + "," + col + ") Exit";
    }

}
