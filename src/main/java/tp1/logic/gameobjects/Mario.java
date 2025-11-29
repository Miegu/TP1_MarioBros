package tp1.logic.gameobjects;

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
        Position pos = getPosition();

        if (!pos.isValidPosition() || (big && new Position(pos.getRow() + 1, pos.getCol()).getRow() > 14)) {
            // Si Mario es grande y la casilla superior (row-1) está fuera
            game.marioDies();
            return;
        }
        //TODO fill your code
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

    private void applyGravity() {
        Position pos = getPosition();
        Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
        //Si se sale del tablero muere
        if (!debajo.isValidPosition()) {
            game.marioDies();
            return;
        }
        if (!pos.isValidPosition()) {
            game.marioDies();
            return;
        }
        //Si no hay suelo cae
        if (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            setPosition(debajo);
            isFalling = true;
            hasMovedThisTurn = true;
        }

    }

    //Comprueba si Mario esta en el suelo
    private boolean isOnGround() {
        Position pos = getPosition();
        Position below = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
        return !below.isValidPosition() || game.getGameObjects().isSolid(below);
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
                if(isOnGround()){
                   direccion = 0; //Queda en STOP
                }else{
                   while (!isOnGround()) {
                     applyGravity();
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
        Position pos = getPosition();
        if (direccion == 0) {
            return; //Estamos en STOP;
        }
        
        if (!isOnGround() && !hasMovedThisTurn) {
            applyGravity();
            game.doInteractionsFrom(this);
        }
        Position newPos = pos.move(0, direccion);

        if (canMoveTo(newPos)) {
            setPosition(newPos);
        } else {
            direccion = -direccion; //Cambia de direccion si se choca
            facingRight = (direccion == 1);
        }
    }
    @Override
    protected boolean canMoveTo(Position position) {
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        // Verifica la posición principal
       if (!game.isInside(position) || game.isSolid(position)) {
            return false;
        }

    public boolean interactWith(ExitDoor door) {
        if (isInPosition(door.getPosition())) {
            game.marioExited();
            return true;
        }

        return true;
    }
    public boolean receiveInteraction(Goomba goomba) {
        // Mario recibe daño del Goomba
        game.marioDies();
        return true;
    }

    // También agregar un getter público para isFalling si no existe
    public boolean isFalling() {
        return isFalling;
    }

    // Metodos para interactuar con otros objetos
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

        if (!isInPosition(goomba.getPosition())) {
            return false;
        }
        if (!goomba.estaVivo()) {
            return false;
        }
        //Esta saltando sobre el wombat?

        if (isFalling) {
            //El goomba muere RIP
            goomba.receiveInteraction(this);
            game.addPoints(100);
        }else{
            if(this.isBig()){
                //Se hace pequeño, pero no muere
                setBig(false);
                //El si que muere goomba 
                goomba.receiveInteraction(this);
                game.addPoints(100);
            }else{
                //El goomba tambien muere?? @PETA
                goomba.receiveInteraction(this);
                //Mario pequeño muere
                game.marioDies();
                //Esto ya es morboso
                game.addPoints(100);
            }
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
        Position pos = getPosition();
        if (pos.equals(position)) {
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
    public GameObject parse(String[] objWords, GameWorld game) {
       // Formato: (fila,col) MARIO [LEFT|RIGHT|STOP|L|R|S] [BIG|SMALL|B|S]
    if (objWords.length < 2) return null;
    
    String type = objWords[1].toUpperCase();
    if (!type.equals("MARIO") && !type.equals("M")) {
        return null;
    }
    
    // Parsear posición
    Position pos = parsePosition(objWords[0]);
    if (pos == null) return null;
    
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
        }
    }
    
    return mario;
}

    // Método auxiliar para parsear posición
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
    
    //Crear funcion serialize que admita (f,col)MARIO dir tamaño
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

        String sizeStr;

        if (big) {
            sizeStr = "BIG";
        } else {
            sizeStr = "SMALL";
        }

        return "(" + row + "," + col + ") Mario " + dirStr + " " + sizeStr;
    }

}