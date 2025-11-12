package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

//Mario class extiende de GameObject, es su hijo
public class Mario extends GameObject {

    private Action direction;
    private boolean big;
    private ActionList accionesPendientes;
    private boolean hasMovedThisTurn; //Para saber si ya se ha movido en el turno actual;
    private boolean isFalling;

    protected Mario() {
        super();
    }

    //Constructor de Mario
    public Mario(GameWorld game, Position position) {
        super(game, position);
        this.direction = Action.RIGHT; //Por defecto mira a la derecha
        this.big = true; //Por defecto es grande
        this.accionesPendientes = new ActionList();
        this.hasMovedThisTurn = false;
    }

    /**
     * Implements the automatic update
     */
    @Override
    public void update() {
        if (!game.isInside(getPosition())) {
            game.loseLife();
            return;
        }

        // Chequea si Mario se sale por arriba si es grande
        if (big && !game.isInside(getPosition().up())) {
            game.loseLife();
            return;
        }

        boolean playerHasActions = !accionesPendientes.isEmpty();
        isFalling = false;
        hasMovedThisTurn = false;

        //1. Primero procesamos las acciones
        if (playerHasActions) {
            processPlayerActions();
        }

        //2. Si no se ha movido, movimiento automatico
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
        }

        //3. Chequea? Checkea? Comprueba si interactua con algo (Wombat? MAybe)
        //Ahora se hace desde gameobjectcontainer
        if (!game.isInside(getPosition())) {
            game.loseLife();
        }
    }

    //Procesa las acciones del jugador
    private void processPlayerActions() {
        for (Action action : accionesPendientes.getActions()) {
            executeAction(action);
        }

        accionesPendientes.clear();
    }

    private void applyGravity() {
        Position currentPos = getPosition();
        Position below = currentPos.down();

        if (!game.isInside(below)) {
            setPosition(below);
            return;
        }

        if (!game.isSolid(below)) {
            setPosition(below);
            isFalling = true;
            hasMovedThisTurn = true;
            if (!game.isInside(getPosition())) {
                game.loseLife();
            }
        }
    }

    //Comprueba si Mario esta en el suelo
    private boolean isOnGround() {
        Position currentPos = getPosition();
        Position below = currentPos.down();

        // Si Mario está fuera del tablero, NO está en el suelo
        if (!game.isInside(currentPos)) {
            return false;
        }

        // Si below está fuera del tablero, Mario NO está en suelo (debe caer)
        if (!game.isInside(below)) {
            return false;
        }

        // Está en suelo solo si below es sólido
        boolean result = game.isSolid(below);
        return result;
    }

    //Ejecuta una accion
    private void executeAction(Action action) {
        Position pos = getPosition();
        Position newPos;

        switch (action) {
            case LEFT:
                newPos = pos.left();
                if (canMoveTo(newPos)) {
                    setPosition(newPos);
                    hasMovedThisTurn = true;
                }
                direction = Action.LEFT;
                break;

            case RIGHT:
                newPos = pos.right();
                if (canMoveTo(newPos)) {
                    setPosition(newPos);
                    hasMovedThisTurn = true;
                }
                direction = Action.RIGHT;
                break;

            case UP:
                newPos = pos.up();
                if (canMoveTo(newPos)) {
                    setPosition(newPos);
                    hasMovedThisTurn = true;
                }
                break;

            case DOWN:
                if (!isOnGround()) {
                    applyGravity();
                } else {
                    direction = Action.STOP;
                }
                break;

            case STOP:
                direction = Action.STOP;
                break;
        }

    }

    // Movimiento automatico version nueva y renovada
    private void performAutomaticMovement() {
        if (!isOnGround()) {
            applyGravity();
        }
        if (direction != Action.STOP && !isFalling) {
            Position newPos = (direction == Action.RIGHT) ? pos.right() : pos.left();
            if (canMoveTo(newPos)) {
                setPosition(newPos);
                hasMovedThisTurn = true;
                if (!game.isInside(getPosition())) {
                    game.loseLife();
                }
            } else {
                direction = (direction == Action.RIGHT) ? Action.LEFT : Action.RIGHT;
            }
        }
    }

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }
    //INteractua con la puerta de salida

    @Override
    public boolean receiveInteraction(ExitDoor door) {
        // Solo gana si está en la misma posición
        if (isInPosition(door.getPosition())) {
            game.marioReachedExit();
            return true;
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Goomba goomba) {
        return false;
    }

    // También agregar un getter público para isFalling si no existe
    public boolean isFalling() {
        return isFalling;
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
        return false;
    }

    /**
     * Añadir Accion a la lista de acciones
     *
     */
    public void addAction(Action action) {
        accionesPendientes.addAction(action);
    }

    private boolean canMoveTo(Position position) {
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        // Verifica la posición principal
        if (!game.isInside(position) || game.isSolid(position)) {
            return false;
        }

        // Si Mario es grande, verifica también la celda de arriba
        if (big) {
            Position above = position.up();
            if (!game.isInside(above) || game.isSolid(above)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean shouldUpdateInLoop() {
        return false; //Mario se actualiza desde game
    }

    @Override
    public String getIcon() {
        switch (direction) {
            case LEFT:
                return Messages.MARIO_LEFT;
            case RIGHT:
                return Messages.MARIO_RIGHT;
            case STOP:
                return Messages.MARIO_STOP;
            default:
                return Messages.MARIO_RIGHT;
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    public boolean isBig() {
        return big;
    }

    public void setBig(boolean big) {
        this.big = big;
    }

    public void receiveDamage() {
        if (isBig()) {
            setBig(false);
        } else {
            game.loseLife();
        }
    }

    @Override
    public boolean isInPosition(Position position) {
        Position currentPos = getPosition();
        if (currentPos.equals(position)) {
            return true;
        }

        if (big) {
            Position above = currentPos.up();
            return above.equals(position);
        }
        return false;
    }

    @Override
    public boolean canBeRemoved() {
        return false;  // Mario nunca se elimina
    }

    @Override
    public String toString() {
        return "Mario at " + pos.toString();
    }
}
