package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Clase abstracta que representa objetos móviles del juego (Mario, Goomba, Mushroom).
 * 
 * Proporciona funcionalidad común para objetos que:
 *   -Se mueven horizontalmente en una dirección
 *   -Son afectados por la gravedad
 *   -Pueden caer y aterrizar sobre superficies sólidas
 *   -Necesitan verificar si las posiciones son transitable
 * 
 * Las subclases deben implementar:
 *   {@link #handleOutOfBounds()} - Comportamiento al salir del tablero
 *   Todos los métodos abstractos de GameObject
 *
 */
public abstract class MovingObject extends GameObject {

    /** Dirección actual de movimiento (típicamente LEFT o RIGHT) */
    protected Action direction;
    protected boolean isFalling;

    /**
     * Construye un MovingObject con un mundo de juego y posición inicial. La
     * dirección por defecto es RIGHT, no está cayendo inicialmente.
     *
     * @param game El mundo del juego
     * @param pos La posición inicial
     */
    public MovingObject(GameWorld game, Position pos) {
        super(game, pos);
        this.isFalling = false;
        this.direction = Action.RIGHT;
    }

     /**
     * Constructor protegido sin argumentos para el patrón Factory.
     * Crea un objeto móvil sin inicializar.
     */
    protected MovingObject() {
        super();
        this.isFalling = false;
        this.direction = Action.RIGHT;
    }

    // ==================== GRAVEDAD Y DETECCIÓN DE SUELO ====================
    
    /**
     * Aplica la gravedad a este objeto.
     * Si no hay superficie sólida debajo, el objeto cae una posición hacia abajo.
     * Si el objeto cae fuera de los límites, se llama a {@link #handleOutOfBounds()}.
     */

    protected void applyGravity() {
        Position currentPos = getPosition();
        Position below = currentPos.down();

        //Si está fuera del tablero, manejar según el tipo de objeto
        if (!game.isInside(currentPos)) {
            handleOutOfBounds();
            return;
        }

        //Si la posicion inferior está fuera, el objeto debe caer/morir
        if (!game.isInside(below)) {
            setPosition(below);
            isFalling = true;
            handleOutOfBounds();
            return;
        }

        //Si no hay nada sólido debajo, cae
        if (!game.isSolid(below)) {
            setPosition(below);
            isFalling = true;
        } else {
            isFalling = false; //Deja de caer cuando toca suelo
        }
    }

    /**
     * Verifica si este objeto está sobre suelo sólido.
     * Un objeto está en el suelo si hay una posición sólida directamente debajo.
     * 
     * @return true si está sobre suelo sólido, false si está en el aire o fuera de límites
     */
    protected boolean isOnGround() {
        Position currentPos = getPosition();

        //Si está fuera del tablero, no esta en el suelo
        if (!game.isInside(currentPos)) {
            return false;
        }

        Position below = currentPos.down();

        // No está en el suelo si la posición inferior está fuera de los límites
        if (!game.isInside(below)) {
            return false;
        }

        //Solo está en el suelo si below es sólido
        return game.isSolid(below);
    }

    // ==================== UTILIDADES DE MOVIMIENTO ====================
    
    /**
     * Verifica si este objeto puede moverse a una posición dada.
     * Una posición es transitable si está dentro del tablero y no es sólida.
     * 
     * @param position La posición objetivo a verificar
     * @return true si el objeto puede moverse allí, false en caso contrario
     */
    protected boolean canMoveTo(Position position) {
        return game.isInside(position) && !game.isSolid(position);
    }

    /**
     * Intenta mover este objeto en la dirección especificada.
     * El movimiento solo ocurre si la posición destino es transitable (no sólida).
     * 
     * <p>Este método combina el cálculo de la nueva posición con la verificación
     * de colisiones usando {@link #canMoveTo(Position)}.
     * 
     * @param dir La dirección en la que moverse
     * @return true si el movimiento fue exitoso, false si fue bloqueado
     */
    protected boolean move(Action dir) {
        if (dir == null) {
            return false;
        }
        
        Position newPos = getPosition().move(dir);
        
        if (canMoveTo(newPos)) {
            setPosition(newPos);
            return true;
        }
        
        return false;
    }

    /**
     * Maneja el comportamiento cuando este objeto sale de los límites del tablero.
     * Diferentes objetos reaccionan de forma distinta (Mario pierde vida, enemigos mueren, etc.)
     * 
     * Este método es llamado por {@link #applyGravity()} cuando el objeto cae fuera del área de juego válida.
     */
    protected abstract void handleOutOfBounds();

    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene si este objeto está cayendo actualmente.
     * 
     * @return true si está cayendo, false si está en el suelo
     */
    public boolean isFalling() {
        return isFalling;
    }

    // Getter para la dirección
    public Action getDirection() {
        return direction;
    }

    // Setter para la dirección
    protected void setDirection(Action direction) {
        this.direction = direction;
    }
    
    // ==================== UTILIDADES DE PARSING ====================
    
    /**
     * Parsea elementos comunes de objetos móviles: posición y dirección.
     * 
     * <p>Devuelve un array con:
     * <ul>
     *   <li>[0] - Position parseada</li>
     *   <li>[1] - Action de dirección (o null si no se especifica)</li>
     * </ul>
     * 
     * @param objWords Array de palabras del objeto
     * @param expectedTypes Tipos válidos (ej: "GOOMBA", "G")
     * @return Array con [Position, Action], o null si no es este tipo
     * @throws ObjectParseException si el formato es inválido
     */
    protected Object[] parseMovingCommon(String[] objWords, String... expectedTypes) 
            throws ObjectParseException {
        
        // Parsear posición usando el método de GameObject
        Position pos = parseCommon(objWords, expectedTypes);
        if (pos == null) {
            return null; // No es este tipo de objeto
        }
        
        // Dirección opcional (si hay un tercer argumento)
        Action direction = null;
        if (objWords.length > 2) {
            direction = parseDirection(objWords[2], objWords);
        }
        
        return new Object[] { pos, direction };
    }

    /**
     * Parsea la dirección de movimiento desde un string.
     * 
     * <p>Direcciones válidas:
     * <ul>
     *   <li>LEFT, L - Movimiento a la izquierda</li>
     *   <li>RIGHT, R - Movimiento a la derecha</li>
     *   <li>STOP, S - Sin movimiento</li>
     *   <li>UP, U - Movimiento hacia arriba</li>
     *   <li>DOWN, D - Movimiento hacia abajo</li>
     * </ul>
     * 
     * @param dirStr String que representa la dirección
     * @param objWords Array completo del objeto (para mensajes de error)
     * @return La Action correspondiente
     * @throws ObjectParseException si la dirección es inválida
     */
    protected static Action parseDirection(String dirStr, String[] objWords) 
            throws ObjectParseException {
        
        String upperDir = dirStr.toUpperCase();
        
        switch (upperDir) {
            case "LEFT":
            case "L":
                return Action.LEFT;
                
            case "RIGHT":
            case "R":
                return Action.RIGHT;
                
            case "STOP":
            case "S":
                return Action.STOP;
                
            case "UP":
            case "U":
                return Action.UP;
                
            case "DOWN":
            case "D":
                return Action.DOWN;
                
            default:
                throw new ObjectParseException(
                    Messages.ERROR_UNKNOWN_MOVING_DIRECTION.formatted(String.join(" ", objWords))
                );
        }
    }
}
