package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goomba extends MovingObject {

    protected Goomba() {
        super();
    }

    public Goomba(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = Action.LEFT; //Empieza mirando a la izquierda
    }

    @Override
    protected void handleOutOfBounds() {
        // Cuando el Goomba sale del tablero, muere
        dead();
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        // 1: Apply gravity
        applyGravity();
        // 2: Horizontal movement if not falling
        if (!isFalling) {
            Position currentPos = getPosition();
            Position newPos;
            if (direction == Action.LEFT) {
                newPos = currentPos.left();
            } else {
                newPos = currentPos.right();
            }

            if(canMoveTo(newPos)){
                setPosition(newPos);
            }else{
                //Cambiar direccion si choca con algo
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
        //Mario maneja toda la lógica de la interacción
        return false;
    }
    // OTROS METODOS
    @Override
    public String toString() {
        return "Goomba at " + getPosition().toString();
    }

    @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public GameObject parse(String[] objWords, GameWorld game) {
        // Formato: (fila,col) GOOMBA [LEFT|RIGHT|L|R]
        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("GOOMBA") && !type.equals("G")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0]);
        if (pos == null) return null;
        
        Goomba goomba = new Goomba(game, pos);
        
        // Dirección opcional (por defecto LEFT)
        if (objWords.length >= 3) {
            String dir = objWords[2].toUpperCase();
            if (dir.equals("RIGHT") || dir.equals("R")) {
                goomba.direction = Action.RIGHT; // Derecha
            } else if (dir.equals("LEFT") || dir.equals("L")) {
                goomba.direction = Action.LEFT; // Izquierda
            }
        }
        
        return goomba;
    }

    private Position parsePosition(String posStr) {
        try {
        // Formato: (fila,columna)
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
