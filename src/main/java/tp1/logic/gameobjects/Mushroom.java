package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mushroom extends MovingObject {
    
    protected Mushroom() {
        super();
        this.direction = Action.RIGHT;
    }
    
    public Mushroom(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = Action.RIGHT;
    }

    @Override
    protected void handleOutOfBounds() {
        // Cuando el Mushroom sale del tablero, muere
        dead();
    }

    @Override
    public void update() {
        if (!isAlive()){
            return;
        }

        // 1. Gravedad
        applyGravity();
        
        // 2. Movimiento horizontal (si no está cayendo)
        if (!isFalling) {
            Position currentPos = getPosition();
            Position newPos;

            if (direction == Action.LEFT) {
                newPos = currentPos.left();
            } else {
                newPos = currentPos.right();
            }

            if (canMoveTo(newPos)) {
                setPosition(newPos);
            } else {
                // Cambiar dirección si choca con algo
                direction = (direction == Action.LEFT) ? Action.RIGHT : Action.LEFT;
            }
        }
    }

    //METODOS DE INTERACCION
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
       return false;
    }

    //OTROS METODOS

    @Override
    public String getIcon() {
        return Messages.MUSHROOM;
    }
    
    @Override
    public boolean isSolid() {
        return false;
    }
    
     @Override
    public String toString() {
        return "Mushroom at " + getPosition().toString();
    }

    @Override
    public GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException{
        // Formato: (fila,col) MUSHROOM o MU
        if (objWords.length > 3) {
            throw new ObjectParseException(
                Messages.ERROR_OBJECT_PARSE_TOO_MANY_ARGS.formatted(String.join(" ", objWords))
            );
        }

        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("MUSHROOM") && !type.equals("MU")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0], objWords);
        Mushroom mushroom = new Mushroom(game, pos);
        
        if (objWords.length >= 3) {
            String dir = objWords[2].toUpperCase();
            switch (dir) {
                case "RIGHT":
                case "R":
                    mushroom.direction = Action.RIGHT;
                    break;
                case "LEFT":
                case "L":
                    mushroom.direction = Action.LEFT;
                    break;
                default:
                    throw new ObjectParseException(
                        Messages.ERROR_INVALID_MOVING_DIRECTION.formatted(String.join(" ", objWords))
                    );
            }
        }
        
        return mushroom;
    }
    

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String dirStr = (direction == Action.LEFT) ? "LEFT" : "RIGHT";
        return "(" + row + "," + col + ") Mushroom " + dirStr;
    }

    
   
}
