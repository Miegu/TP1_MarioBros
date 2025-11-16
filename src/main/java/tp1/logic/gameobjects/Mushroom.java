package tp1.logic.gameobjects;

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
        // Solo interactúa si están en la misma posición
        if (!mario.isInPosition(this.getPosition())) {
            return false;
        }
        
        // Mario recoge la seta, si es pequeño, se hace grande
        if (!mario.isBig()) {
            mario.setBig(true);
        }
        // Si Mario ya es grande, no le pasa nada
        // Mushroom siempre desaparece
        
        dead();
        return true;
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
    public GameObject parse(String[] objWords, GameWorld game) {
        // Formato: (fila,col) MUSHROOM o MU
        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("MUSHROOM") && !type.equals("MU")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0]);
        if (pos == null) return null;
        
        return new Mushroom(game, pos);
    }
    
    private Position parsePosition(String posStr) {
        try {
            posStr = posStr.replace("(", "").replace(")", "");
            String[] parts = posStr.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new Position(col, row);
        } catch (Exception e) {
            return null;
        }
    }
    
   
}
