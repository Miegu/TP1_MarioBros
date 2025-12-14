package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Representa una seta power-up del juego.
 * 
 * Características:
 *   Se mueve horizontalmente (derecha por defecto)
 *   Cambia de dirección al chocar con obstáculos
 *   Es afectado por la gravedad
 *   Hace crecer a Mario cuando lo toca
 *   Desaparece después de ser recogida
 *   Se elimina del juego cuando muere
 * 
 * Formato de parseo:
 *   {@code (fila,col) MUSHROOM} - Dirección RIGHT por defecto
 *   {@code (fila,col) MU} - Dirección RIGHT por defecto
 *   {@code (fila,col) MUSHROOM LEFT} o {@code (fila,col) MUSHROOM L}
 *   {@code (fila,col) MUSHROOM RIGHT} o {@code (fila,col) MUSHROOM R}
 *
 */
public class Mushroom extends MovingObject {
    
    /**
     * Construye un Mushroom en la posición especificada.
     * La dirección por defecto es RIGHT.
     * 
     * @param game El mundo del juego
     * @param pos La posición inicial del Mushroom
     */
    public Mushroom(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = Action.RIGHT; // Mushroom empieza moviéndose a la derecha
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected Mushroom() {
        super();
        this.direction = Action.RIGHT;
    }

    // ==================== PROPIEDADES DEL OBJETO ====================
    @Override
    public String getIcon() {
        return Messages.MUSHROOM;
    }

    @Override
    public boolean isSolid() {
        return false; // Mushroom no es sólido, se puede atravesar
    }

    @Override
    protected void handleOutOfBounds() {
        // Cuando el Mushroom sale del tablero, muere
        dead();
    }

    // ==================== ACTUALIZACIÓN ====================

    @Override
    public void update() {
        if (!isAlive()) {
            return; // Si no está vivo, no hace nada
        }

        // 1. Aplicar gravedad
        applyGravity();

        game.doInteractionsFrom(this);
        // 2. Movimiento horizontal (solo si no está cayendo)
        if (!isFalling) {
            performHorizontalMovement();
        }
    }

    /**
     * Realiza el movimiento horizontal del Mushroom.
     * Si choca con un obstáculo, cambia de dirección.
     */
    private void performHorizontalMovement() {
        // Intentar moverse en la dirección actual
        if (!move(direction)) {
            // Si el movimiento falló (chocó con algo), cambiar dirección
            direction = (direction == Action.LEFT) ? Action.RIGHT : Action.LEFT;
        }else{
            game.doInteractionsFrom(this);
        }
    }

    // ==================== INTERACCIONES ====================

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
       // Verificar si Mario está en la misma posición que el Mushroom
        if (!mario.isInPosition(getPosition())) {
            return false;
        }

        // Mario recoge el Mushroom y crece si es pequeño
        if (!mario.isBig()) {
            mario.setBig(true); // Mario se hace grande
        }

        // El Mushroom desaparece después de ser recogido
        dead();
        return true;
    }
    
    // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "Mushroom at " + getPosition().toString();
    }
    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea un Mushroom desde su descripción en string.
     * Usa covarianza para devolver el tipo específico Mushroom.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de Mushroom, o null si no es un Mushroom
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
     @Override
    public Mushroom parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear elementos comunes de objetos móviles (posición + dirección)
        Object[] parsed = parseMovingCommon(objWords, "MUSHROOM", "MU");
        if (parsed == null) {
            return null; // No es un Mushroom
        }

        Position pos = (Position) parsed[0];
        Action direction = (Action) parsed[1];

        // Validar que no hay argumentos extra
        validateMaxArgs(objWords, 3);

        // Crear Mushroom
        Mushroom mushroom = new Mushroom(game, pos);

        // Establecer dirección si se especificó
        if (direction != null) {
            mushroom.setDirection(direction);
        }

        return mushroom;
    }
    

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String dirStr = (direction == Action.LEFT) ? "LEFT" : "RIGHT";
        return "(" + row + "," + col + ") Mushroom " + dirStr;
    }

    
   
}
