package tp1.logic.gameobjects;

import tp1.exceptions.ActionParseException;
import tp1.exceptions.ObjectParseException;
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
        game.doInteractionsFrom(this);

        // 2: Horizontal movement if not falling
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
                game.doInteractionsFrom(this);
            } else {
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
    public GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Formato: (fila,col) GOOMBA [LEFT|RIGHT|L|R]
        System.out.println("=== GOOMBA PARSE DEBUG ===");
        System.out.println("objWords.length = " + objWords.length);
        for (int i = 0; i < objWords.length; i++) {
            System.out.println("objWords[" + i + "] = '" + objWords[i] + "'");
        }
        System.out.println("==========================");
        
        if (objWords.length < 2) {
            return null;
        }

        String type = objWords[1].toUpperCase();
        if (!type.equals("GOOMBA") && !type.equals("G")) {
            return null;
        }

        Position pos = parsePosition(objWords[0], objWords);

        Goomba goomba = new Goomba(game, pos);

        // Dirección opcional (por defecto LEFT)
        if (objWords.length >= 3) {
            String dir = objWords[2].toUpperCase();
            boolean validDirection = false;
            switch (dir) {
                case "RIGHT":
                case "R":
                    goomba.direction = Action.RIGHT;
                    validDirection = true;
                    break;
                case "LEFT":
                case "L":
                    goomba.direction = Action.LEFT;
                    validDirection = true;
                    break;
            }
            if (!validDirection) {
                try {
                    Action.parse(dir);
                    // Si llegamos aquí, es una acción válida pero no para Goomba
                    throw new ObjectParseException(
                            Messages.ERROR_INVALID_MOVING_DIRECTION.formatted(String.join(" ", objWords))
                    );
                } catch (ActionParseException e) {
                    // Es una dirección completamente desconocida
                    ObjectParseException innerException = new ObjectParseException(
                            Messages.UNKNOWN_ACTION.formatted(dir),
                            e
                    );
                    throw new ObjectParseException(
                            Messages.ERROR_UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", objWords)),
                            innerException
                    );
                }
            }
        }

        if (objWords.length > 3) {
            throw new ObjectParseException(
                    Messages.ERROR_OBJECT_PARSE_TOO_MANY_ARGS.formatted(String.join(" ", objWords))
            );
        }

        return goomba;
    }

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        String dirStr = (direction == Action.LEFT) ? "LEFT" : "RIGHT";

        return "(" + row + "," + col + ") Goomba " + dirStr;
    }

}
