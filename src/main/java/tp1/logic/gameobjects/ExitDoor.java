package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;
/**
 * Representa la puerta de salida del nivel.
 * 
 * Características:
 *   - No es sólida - Mario puede atravesarla
 *   - No se puede eliminar del juego
 *   - Cuando Mario llega a ella, gana el nivel
 * 
 * Formato de parseo: {@code (fila,col) EXITDOOR} o {@code (fila,col) ED}
 */
public class ExitDoor extends GameObject {

    /**
     * Construye una ExitDoor en la posición especificada.
     * 
     * @param game El mundo del juego
     * @param pos La posición donde se encuentra la puerta
     */
    public ExitDoor(GameWorld game, Position pos) {
        super(game, pos);
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected ExitDoor() {
        super();
    }

    // ==================== PROPIEDADES DEL OBJETO ====================

    @Override
    public String getIcon() {
        return Messages.EXIT_DOOR;
    }

    @Override
    public boolean isSolid() {
        return false; // ExitDoor no es sólida, Mario puede atravesarla
    }

    @Override
    public boolean canBeRemoved() {
        return false;
    }

    // ==================== INTERACCIONES ====================

    @Override
    public boolean interactWith(GameItem other) {
        // La puerta permite que otros interactúen con ella
        return other.receiveInteraction(this);
    }

    @Override
    public boolean receiveInteraction(PlayableObject player) {
        // Mario ha llegado a la puerta de salida
        if (player.isInPosition(this.getPosition())) {
            game.marioReachedExit();
            return true;
        }
        return false;
    }
    // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "ExitDoor at " + getPosition().toString();
    }

    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea una ExitDoor desde su descripción en string.
     * Usa covarianza para devolver el tipo específico ExitDoor en lugar de GameObject.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de ExitDoor, o null si no es una ExitDoor
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */

    @Override
    public ExitDoor parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear elementos comunes (posición + validación de tipo)
        Position pos = parseCommon(objWords, "EXITDOOR", "ED", "EXIT");
        if (pos == null) {
            return null; // No es una ExitDoor
        }
        
        // Validar que no hay argumentos extra
        validateMaxArgs(objWords, 2);
        
        return new ExitDoor(game, pos);
    }

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        return "(" + row + "," + col + ") Exit";
    }

}
