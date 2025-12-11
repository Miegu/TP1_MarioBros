package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Representa un enemigo Goomba del juego.
 * 
 * Características:
 *   Se mueve horizontalmente (izquierda por defecto)
 *   Cambia de dirección al chocar con obstáculos
 *   Es afectado por la gravedad
 *   Muere cuando Mario salta sobre él
 *   Mata a Mario si lo toca lateralmente
 *   Se elimina del juego cuando muere
 * 
 * Formato de parseo:
 *   {@code (fila,col) GOOMBA} - Dirección LEFT por defecto
 *   {@code (fila,col) G} - Dirección LEFT por defecto
 *   {@code (fila,col) GOOMBA LEFT} o {@code (fila,col) GOOMBA L}
 *   {@code (fila,col) GOOMBA RIGHT} o {@code (fila,col) GOOMBA R}
 */
public class Goomba extends MovingObject {

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected Goomba() {
        super();
    }

    /**
     * Construye un Goomba en la posición especificada.
     * La dirección por defecto es LEFT.
     * 
     * @param game El mundo del juego
     * @param pos La posición inicial del Goomba
     */
    public Goomba(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = Action.LEFT; //Empieza mirando a la izquierda
    }

    // ==================== PROPIEDADES DEL OBJETO ====================
    
     @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    protected void handleOutOfBounds() {
        // Cuando el Goomba sale del tablero, muere
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
     * Realiza el movimiento horizontal del Goomba.
     * Si choca con un obstáculo, cambia de dirección.
     */
    private void performHorizontalMovement() {
        // Intentar moverse en la dirección actual
        if (!move(direction)) {
            // Si el movimiento falló (chocó con algo), cambiar dirección
            direction = (direction == Action.LEFT) ? Action.RIGHT : Action.LEFT;
        } else {
            // Movimiento exitoso, verificar interacciones
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
        // Verificar si están en la misma posición
        if (!isInPosition(mario.getPosition())) {
            return false;
        }

        // Si Mario está cayendo, aplasta al Goomba
        if (mario.isFalling()) {
            dead(); // Goomba muere
            game.addScore(100); // 100 puntos por matar al Goomba
            return true;
        } else {
            // Mario toca al Goomba lateralmente - Mario recibe daño
            mario.receiveDamage();
            dead(); // El Goomba también muere en el choque
            game.addScore(100); // 100 puntos por matar al Goomba
            return true;
        }
    }

     // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "Goomba at " + getPosition().toString();
    }

    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea un Goomba desde su descripción en string.
     * Usa covarianza para devolver el tipo específico Goomba.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de Goomba, o null si no es un Goomba
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
    @Override
    public Goomba parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear elementos comunes de objetos móviles (posición + dirección)
        Object[] parsed = parseMovingCommon(objWords, "GOOMBA", "G");
        if (parsed == null) {
            return null; // No es un Goomba
        }

        Position pos = (Position) parsed[0];
        Action direction = (Action) parsed[1];

        // Validar que no hay argumentos extra
        validateMaxArgs(objWords, 3);

        // Crear Goomba
        Goomba goomba = new Goomba(game, pos);

        // Establecer dirección si se especificó
        if (direction != null) {
            goomba.setDirection(direction);
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
