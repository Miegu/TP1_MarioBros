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

    protected Mario(){
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
        Position currentPos = getPosition();

       if (!game.isInside(currentPos)) {
            game.loseLife();
            return;
        }
        
        // Chequea si Mario se sale por arriba si es grande
        if (big && !game.isInside(currentPos.up())) {
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

    }

    //Procesa las acciones del jugador
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
            game.loseLife();
            return;
        }
        
        if (!game.isSolid(below)) {
            setPosition(below);
            isFalling = true;
            hasMovedThisTurn = true;
        }
    }

    //Comprueba si Mario esta en el suelo
    private boolean isOnGround() {
        Position currentPos = getPosition();
        Position below = currentPos.down();
        return !game.isInside(below) || game.isSolid(below);
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

    // Movimiento automatico version nueva y renovada
    private void performAutomaticMovement() {
        if (direction == Action.STOP) {
            return;
        }
        
        if (!isOnGround() && !hasMovedThisTurn) {
            applyGravity();
            return;
        }
        
        Position newPos = (direction == Action.RIGHT) ? pos.right() : pos.left();

        if (canMoveTo(newPos)) {
            setPosition(newPos);
        } else {
            // Change direction on collision
            direction = (direction == Action.RIGHT) ? Action.LEFT : Action.RIGHT;
        }
    }
    //INteractua con la puerta de salida

    public boolean interactWith(ExitDoor door) {
        if (isInPosition(door.getPosition())) {
            //Mario ha llegado a la puerta de salida
            return true;
        }
        return false;
    }

    @Override
    public boolean receiveInteraction(Goomba goomba) {
        // Mario recibe daño del Goomba
        game.loseLife();
        return true;
    }

    // También agregar un getter público para isFalling si no existe
    public boolean isFalling() {
        return isFalling;
    }

    // interactua con el Gomba? Siempre les he dicho Wombats
    public boolean interactWith(Goomba goomba) {

       if (!isInPosition(goomba.getPosition()) || !goomba.isAlive()) {
            return false;
        }

        if (isFalling) {
            goomba.receiveInteraction(this);
            game.addScore(100);
        } else {
            if (this.isBig()) {
                setBig(false);
                goomba.receiveInteraction(this);
                game.addScore(100);
            } else {
                goomba.receiveInteraction(this);
                game.loseLife();
                game.addScore(100);
            }
        }
        return true;
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
        return position.isValidPosition() && !game.isSolid(position);
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

}
