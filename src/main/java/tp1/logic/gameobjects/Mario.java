package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mario extends GameObject {

    private Action direction;
    private boolean big;
    private ActionList accionesPendientes;
    private boolean hasMovedThisTurn;
    private boolean isFalling;

    protected Mario(){
        super();
    }
    //Constructor de Mario
    public Mario(GameWorld game, Position position) {
        super(game, position);
        this.direction = Action.RIGHT;
        this.big = true;
        this.accionesPendientes = new ActionList();
        this.hasMovedThisTurn = false;
    }

    @Override
    public void update() {
        Position currentPos = getPosition();

        if (!game.isInside(currentPos)) {
            game.loseLife();
            return;
        }
        if (big && !game.isInside(currentPos.up())) {
            game.loseLife();
            return;
        }
        boolean playerHasActions = !accionesPendientes.isEmpty();
        isFalling = false;
        hasMovedThisTurn = false;
        if (playerHasActions) {
            processPlayerActions();
        }
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
        }
    }

    private void processPlayerActions() {
        if (accionesPendientes.isEmpty()) {
            return;
        }
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
            game.loseLife();
            return;
        }
        if (!game.isSolid(below)) {
            setPosition(below);
            isFalling = true;
            hasMovedThisTurn = true;
        }
    }

    private boolean isOnGround() {
        Position currentPos = getPosition();
        Position below = currentPos.down();
        return !game.isInside(below) || game.isSolid(below);
    }

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
                if(!isOnGround()){
                    while (!isOnGround()) {
                        applyGravity();
                    }
                }else{
                    direction = Action.STOP;
                }
                break;
            case STOP:
                direction = Action.STOP;
                break;
        }
    }

    private void performAutomaticMovement() {
        // Gravedad primero
        if (!isOnGround()) {
            applyGravity();
        }
        if (direction == Action.STOP) {
            return;
        }
        if (!isFalling) {
            Position newPos = (direction == Action.RIGHT) ? pos.right() : pos.left();
            if (canMoveTo(newPos)) {
                setPosition(newPos);
                hasMovedThisTurn = true;
            } else {
                direction = (direction == Action.RIGHT) ? Action.LEFT : Action.RIGHT;
            }
        }
    }

    public boolean interactWith(ExitDoor door) {
        if (isInPosition(door.getPosition())) {
            return door.receiveInteraction(this);
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Goomba goomba) {
        // Siempre que un Goomba interactúe con Mario, Mario pierde vida
        game.loseLife();
        return true;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public boolean interactWith(Goomba goomba) {
        if (!isInPosition(goomba.getPosition()) || !goomba.isAlive()) {
            return false;
        }
        // Simetría: Mario siempre mata al Goomba, caiga o no (por requerimiento)
        goomba.receiveInteraction(this);
        game.addScore(100);
        // Mario solo muere si NO es grande, si es grande, se hace pequeño
        if (isFalling) {
            // Si está cayendo, NO muere, solo suma puntos
            return true;
        }
        if (this.isBig()) {
            setBig(false);
            return true;
        } else {
            game.loseLife();
            return true;
        }
    }

    public void addAction(Action action) {
        accionesPendientes.addAction(action);
    }

    private boolean canMoveTo(Position position) {
        return game.isInside(position) && !game.isSolid(position);
    }

    @Override
    public boolean shouldUpdateInLoop() {
        return false; 
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
        return false; 
    }
    @Override
    public String toString() {
        return "Mario at " + pos.toString();
    }
}
