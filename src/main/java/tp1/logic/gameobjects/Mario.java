package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

//Mario class extiende de GameObject, es su hijo
public class Mario extends MovingObject {

    private boolean big;
    private ActionList accionesPendientes;
    private boolean hasMovedThisTurn; //Para saber si ya se ha movido en el turno actual

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

    @Override
    protected void handleOutOfBounds() {
        // Cuando Mario sale del tablero, pierde una vida
        game.loseLife();
    }
    // Actualiza el estado de Mario
    @Override
    public void update() {

        // Chequea si Mario se sale por arriba si es grande
        if (isBig() && !game.isInside(getPosition().up())) {
            game.loseLife();
            return;
        }

        boolean playerHasActions = !accionesPendientes.isEmpty();
        isFalling = false;
        hasMovedThisTurn = false;

        game.doInteractionsFrom(this);
        
        //1. Primero procesamos las acciones
        if (playerHasActions) {
            processPlayerActions();
        }

        //2. Si no se ha movido, movimiento automatico
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
            game.doInteractionsFrom(this);
        }

        //4. Verificar si está fuera despues del movimiento
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
                    game.doInteractionsFrom(this);
                }
                direction = Action.LEFT;
                break;

            case RIGHT:
                newPos = pos.right();
                if (canMoveTo(newPos)) {
                    setPosition(newPos);
                    hasMovedThisTurn = true;
                    game.doInteractionsFrom(this);
                }
                direction = Action.RIGHT;
                break;

            case UP:
                newPos = pos.up();
                if (canMoveTo(newPos)) {
                    setPosition(newPos);
                    hasMovedThisTurn = true;
                    game.doInteractionsFrom(this);
                }
                break;

            case DOWN:
                if(!isOnGround()){
                    while (!isOnGround()) {
                        applyGravity();
                        game.doInteractionsFrom(this);

                        // Si Mario murió o salió del tablero, detener la caída
                    if (!game.isInside(getPosition())) {
                        break;
                    }
                    } 
                }else {
                        direction = Action.STOP;
                        game.doInteractionsFrom(this);
                    }
                    break;

            case STOP:
                direction = Action.STOP;
                game.doInteractionsFrom(this);
                break;
        }

    }

    // Movimiento automatico version nueva y renovada
    private void performAutomaticMovement() {
        if (!isOnGround()) {
            applyGravity();
            game.doInteractionsFrom(this);
        }
        if (direction != Action.STOP && !isFalling) {
            Position newPos = (direction == Action.RIGHT) ? getPosition().right() : getPosition().left();
            if (canMoveTo(newPos)) {
                setPosition(newPos);
                hasMovedThisTurn = true;
                game.doInteractionsFrom(this);
                if (!game.isInside(getPosition())) {
                    game.loseLife();
                }
            } else {
                direction = (direction == Action.RIGHT) ? Action.LEFT : Action.RIGHT;
            }
        }
    }
    @Override
    protected boolean canMoveTo(Position position) {
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        // Verifica la posición principal
       if (!game.isInside(position) || game.isSolid(position)) {
            return false;
        }

        if (big) {
            Position above = position.up();
            if (!game.isInside(above) || game.isSolid(above)) {
                return false;
            }
        }

        return true;
    }

    // Metodos para interactuar con otros objetos
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    private void defeatEnemy(GameObject enemy, int points) {
        // El enemigo muere
        enemy.dead();
        
        // Mario gana puntos
        game.addScore(points);
        
        // Mario recibe daño SOLO si NO está cayendo
        // (Si está cayendo, es porque saltó sobre el enemigo)
        if (!this.isFalling()) {
            this.receiveDamage();
        }
    }

    @Override
    public boolean receiveInteraction(Goomba goomba) {
        // Solo interactúa si están en la misma posición
        if (!this.isInPosition(goomba.getPosition())) {
            return false;
        }
        // Lógica de derrota del Goomba
        defeatEnemy(goomba, 100);
        return true;
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
    //Interactua con el mushroom
    @Override
    public boolean receiveInteraction(Mushroom mushroom) {
        // Solo interactúa si están en la misma posición
        if (!this.isInPosition(mushroom.getPosition())) {
            return false;
        }
        
        // Lógica del mushroom
        if (!this.isBig()) {
            this.setBig(true);  // Mario se hace grande
        }
        // Si ya es grande, no pasa nada
        
        // Mushroom desaparece
        mushroom.dead();
        
        return true;
    }

    //Interactua con box
    @Override
    public boolean receiveInteraction(Box box) {
        // Box maneja toda la lógica
        return false;
    }

    // OTROS METODOS
    public void addAction(Action action) {
        accionesPendientes.addAction(action);
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
    public void onAdded(GameWorld game) {
        // Cuando un Mario es añadido al juego, se registra como el Mario jugable
        game.registerAsMain(this);
    }

    @Override
    public String toString() {
        return "Mario at " + getPosition().toString();
    }

    @Override
    public GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException {
       // Formato: (fila,col) MARIO [LEFT|RIGHT|STOP|L|R|S] [BIG|SMALL|B|S]

        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("MARIO") && !type.equals("M")) {
            return null;
        }
        
        // Parsear posición
        Position pos = parsePosition(objWords[0], objWords);
        
        // Crear Mario con valores por defecto
        Mario mario = new Mario(game, pos);
    
    // Parsear dirección (si existe)
    if (objWords.length > 2) {
        String dirStr = objWords[2].toUpperCase();
        switch (dirStr) {
            case "LEFT":
            case "L":
                mario.direction = Action.LEFT;
                break;
            case "RIGHT":
            case "R":
                mario.direction = Action.RIGHT;
                break;
            case "STOP":
            case "S":
                mario.direction = Action.STOP;
                break;
            default:
                throw new ObjectParseException(
                    Messages.ERROR_UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", objWords)) + "\n" +
                        Messages.ERROR_UNKNOWN_ACTION.formatted(dirStr)
                );
        }
    }
    
    // Parsear tamaño (si existe)
    if (objWords.length > 3) {
        String sizeStr = objWords[3].toUpperCase();
        switch (sizeStr) {
            case "BIG":
            case "B":
                mario.setBig(true);
                break;
            case "SMALL":
            case "S":
                mario.setBig(false);
                break;
            default:
                throw new ObjectParseException(
                    Messages.ERROR_INVALID_MARIO_SIZE.formatted(String.join(" ", objWords))
                );
        }
    }

    if (objWords.length > 4) {
            throw new ObjectParseException(
                Messages.ERROR_OBJECT_PARSE_TOO_MANY_ARGS.formatted(String.join(" ", objWords))
            );
        }
    
    return mario;
}

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        
        String dirStr;
        switch (direction) {
            case LEFT:  dirStr = "LEFT";  break;
            case RIGHT: dirStr = "RIGHT"; break;
            case STOP:  dirStr = "STOP";  break;
            default:    dirStr = "RIGHT"; break;
        }
        
        String sizeStr = big ? "BIG" : "SMALL";
        
        return "(" + row + "," + col + ") Mario " + dirStr + " " + sizeStr;
    }

}
