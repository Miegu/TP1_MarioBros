package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {

    public Land(GameWorld game, Position pos) {
        super(game, pos);
    }

    protected Land() {
        super();
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
    public boolean interactWith(GameItem other) {
        // Land no interactúa activamente, pero permite que otros interactúen con él
        return other.receiveInteraction(this);
    }

    @Override
    public boolean canBeRemoved() {
        return false;
    }

    @Override
    public String toString() {
        return "Land at " + getPosition().toString();
    }

    
    @Override
    public GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException{
        // Formato: (fila,col) LAND
        if (objWords.length > 2) {
           throw new ObjectParseException(
                Messages.ERROR_OBJECT_PARSE_TOO_MANY_ARGS.formatted(String.join(" ", objWords))
            );
        }

        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("LAND") && !type.equals("L")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0], objWords);
        
        return new Land(game, pos);
    }

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        return "(" + row + "," + col + ") Land";
    }

}
