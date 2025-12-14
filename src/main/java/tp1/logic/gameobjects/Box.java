package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Representa una caja sorpresa del juego.
 * 
 * Características:
 *   Es sólida - bloquea el movimiento
 *   No se puede eliminar del juego
 *   Cuando Mario la golpea desde abajo, genera un Mushroom
 *   Después de ser golpeada, queda vacía y no genera más setas
 *   Otorga 50 puntos cuando se activa
 * 
 * Formato de parseo: 
 *   {@code (fila,col) BOX} - Caja llena por defecto
 *   {@code (fila,col) B} - Caja llena por defecto
 *   {@code (fila,col) BOX FULL} o {@code (fila,col) BOX F} - Caja llena explícita
 *   {@code (fila,col) BOX EMPTY} o {@code (fila,col) BOX E} - Caja vacía
 *
 */
public class Box extends GameObject {
    
    private boolean isEmpty;
    
    /**
     * Construye una Box llena en la posición especificada.
     * 
     * @param game El mundo del juego
     * @param pos La posición donde se encuentra la caja
     */
    public Box(GameWorld game, Position pos) {
        this(game, pos, false); // Por defecto llena
    }
    
    /**
     * Construye una Box en la posición especificada con el estado dado.
     * 
     * @param game El mundo del juego
     * @param pos La posición donde se encuentra la caja
     * @param isEmpty true si la caja está vacía, false si está llena
     */
    public Box(GameWorld game, Position pos, boolean isEmpty) {
        super(game, pos);
        this.isEmpty = isEmpty;
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected Box() {
        super();
        this.isEmpty = false;
    }
    // ==================== PROPIEDADES DEL OBJETO ====================

    @Override
    public String getIcon() {
        return isEmpty ? Messages.EMPTY_BOX : Messages.BOX;
    }
    
    @Override
    public boolean isSolid() {
        return true; // Siempre sólida
    }

    @Override
    public boolean canBeRemoved() {
        return false; // Box nunca se elimina del juego
    }
    
    // ==================== INTERACCIONES ====================

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }
    
    @Override
    public boolean receiveInteraction(PlayableObject player) {
        // Si ya esta vacía, no hace nada
        if (isEmpty) {
            return false; 
        }
        
        // Verificar si Mario golpea desde abajo
        Position below = getPosition().down();

        if (player.isInPosition(below)) {
            // Mario golpeó desde abajo
            freeMushroom();
            isEmpty = true;
            game.addScore(50); // 50 puntos por activar la caja
            return true;
        }
        
        return false;
    }
    
    /**
     * Genera un Mushroom encima de esta caja.
     * El Mushroom aparece en la posición superior a la caja.
     */
    private void freeMushroom() {
        // La seta aparece encima de la caja
        Position above = getPosition().up();

        if (game.isInside(above) && !game.isSolid(above)) {
            Mushroom mushroom = new Mushroom(game, above);
            try {
                game.addObject(mushroom);
            } catch (OffBoardException e) {
                // No debería ocurrir, ya que verificamos isInside
                // Pero si ocurre, el mushroom simplemente no se crea
            }
        }
    }
    
    // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "Box at " + getPosition().toString() + (isEmpty ? " (empty)" : " (full)");
    }

    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea una Box desde su descripción en string.
     * Usa covarianza para devolver el tipo específico Box en lugar de GameObject.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de Box, o null si no es una Box
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
    @Override
    public Box parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear elementos comunes (posición + validación de tipo)
        Position pos = parseCommon(objWords, "BOX", "B");
        if (pos == null) {
            return null; // No es una Box
        }

        // Estado por defecto: llena (isEmpty = false)
        boolean isEmpty = false;

        // Si hay un tercer argumento, parsear el estado
        if (objWords.length >= 3) {
            String state = objWords[2].toUpperCase();
            
            if (state.equals("EMPTY") || state.equals("E")) {
                isEmpty = true;
            } else if (state.equals("FULL") || state.equals("F")) {
                isEmpty = false;
            } else {
                throw new ObjectParseException(
                    Messages.ERROR_INVALID_BOX_STATUS.formatted(String.join(" ", objWords))
                );
            }
        }

        // Validar que no hay demasiados argumentos
        validateMaxArgs(objWords, 3);

        return new Box(game, pos, isEmpty);
    }
    
    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String state = isEmpty ? "EMPTY" : "FULL";
        return "(" + row + "," + col + ") Box " + state;
    }

}

