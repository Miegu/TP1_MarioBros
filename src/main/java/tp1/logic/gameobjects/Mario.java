package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Representa al personaje jugador Mario.
 * 
 * Características:
 *   Controlado por el jugador mediante acciones
 *   Puede ser pequeño o grande (Big Mario)
 *   Ocupa 2 casillas verticales cuando es grande
 *   Puede saltar y moverse horizontalmente
 *   Es afectado por la gravedad
 *   Nunca se elimina del juego (canBeRemoved = false)
 *   Interactúa con todos los objetos del juego
 * 
 * Formato de parseo:
 *   {@code (fila,col) MARIO} - Mario pequeño por defecto
 *   {@code (fila,col) M} - Mario pequeño por defecto
 *   {@code (fila,col) MARIO SMALL} o {@code (fila,col) MARIO S}
 *   {@code (fila,col) MARIO BIG} o {@code (fila,col) MARIO B}
 */
public class Mario extends PlayableObject {

    /**
     * Construye un Mario en la posición especificada.
     * Por defecto es grande y sin acciones pendientes.
     * 
     * @param game El mundo del juego
     * @param pos La posición inicial de Mario
     */
    public Mario(GameWorld game, Position position) {
        super(game, position);
        this.direction = Action.RIGHT; //Por defecto mira a la derecha
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected Mario() {
        super();
        this.direction = Action.RIGHT;
    }

    // ==================== PROPIEDADES DEL OBJETO ====================

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

    // ==================== ACTUALIZACIÓN ====================

    /**
     * Ejecuta una acción individual del jugador.
     * 
     * @param action La acción a ejecutar
     */
    @Override
    protected void executeAction(Action action) {
    switch (action) {
        case LEFT:
            if (move(Action.LEFT)) {
                hasMovedThisTurn = true;
                game.doInteractionsFrom(this);
            }
            direction = Action.LEFT;
            break;

        case RIGHT:
            if (move(Action.RIGHT)) {
                hasMovedThisTurn = true;
                game.doInteractionsFrom(this);
            }
            direction = Action.RIGHT;
            break;

        case UP:
            if (move(Action.UP)) {
                hasMovedThisTurn = true;
                game.doInteractionsFrom(this);
            }
            break;

        case DOWN:
            if (!isOnGround()) {
                // Caer rápidamente hasta el suelo
                while (!isOnGround()) {
                    applyGravity();
                    game.doInteractionsFrom(this);
                    // Si Mario murió o salió del tablero, detener la caída
                    if (!game.isInside(getPosition())) {
                        break;
                    }
                }
            } else {
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

    /**
     * Movimiento automático cuando el jugador no proporciona acciones.
     * Mario continúa en su dirección actual si no está cayendo.
     */
    @Override
    protected void performAutomaticMovement() {
        if (!isOnGround()) {
            applyGravity();
            game.doInteractionsFrom(this);
        }

        if (direction != Action.STOP && !isFalling) {
            if (move(direction)) {  // ← Usa move() heredado
                hasMovedThisTurn = true;
                game.doInteractionsFrom(this);
                if (!game.isInside(getPosition())) {
                    game.loseLife();
                }
            } else {
                // Si no puede moverse, cambiar dirección
                direction = (direction == Action.RIGHT) ? Action.LEFT : Action.RIGHT;
            }
        }
    }
    // ==================== INTERACCIONES ====================

    @Override
    public boolean receiveInteraction(ExitDoor door) {
        // Solo gana si está en la misma posición
        if (isInPosition(door.getPosition())) {
            game.marioReachedExit();
            return true;
        }
        return false;
    }

    // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "Mario at " + getPosition().toString();
    }

    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea un Mario desde su descripción en string.
     * Formato: (fila,col) MARIO [LEFT|RIGHT|STOP] [BIG|SMALL]
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de Mario, o null si no es un Mario
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
    @Override
    public Mario parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear posición usando el método común
        Object[] parsed = parseMovingCommon(objWords, "MARIO", "M");
        if (parsed == null) {
            return null; // No es un Mario
        }

        Position pos = (Position) parsed[0];
        Action direction = (Action) parsed[1]; // Puede ser null

        // Crear Mario con valores por defecto
        Mario mario = new Mario(game, pos);

        // Establecer dirección si se especificó
        if (direction != null) {
            mario.direction = direction;
        }

        // Parsear tamaño (si existe) - índice 3
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

        // Validar que no hay demasiados argumentos
        validateMaxArgs(objWords, 4);

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
