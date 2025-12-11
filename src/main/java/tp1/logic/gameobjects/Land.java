package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * Representa una plataforma sólida del juego (suelo, bloques).
 * 
 * Características:
 *   Es sólido - bloquea el movimiento
 *   No se puede eliminar/morir del juego
 *   No tiene comportamiento activo (update vacío)
 * 
 * Formato de parseo: {@code (fila,col) LAND} o {@code (fila,col) L}
 */
public class Land extends GameObject {

    /**
     * Construye un Land en la posición especificada.
     * 
     * @param game El mundo del juego
     * @param pos La posición donde se encuentra el Land
     */
    public Land(GameWorld game, Position pos) {
        super(game, pos);
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     */
    protected Land() {
        super();
    }

    // ==================== PROPIEDADES DEL OBJETO ====================

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

    @Override
    public boolean isSolid() {
        return true; // Land siempre es sólido
    }

    @Override
    public boolean canBeRemoved() {
        return false; // Land no puede ser eliminado
    }

    // ==================== INTERACCIONES ====================

    @Override
    public boolean interactWith(GameItem other) {
        // Land no interactúa activamente, pero permite que otros interactúen con él
        return other.receiveInteraction(this);
    }

    // ==================== REPRESENTACIÓN ====================

    @Override
    public String toString() {
        return "Land at " + getPosition().toString();
    }

    // ==================== PARSING Y SERIALIZACIÓN ====================

    /**
     * Parsea un Land desde su descripción en string.
     * Usa covarianza para devolver el tipo específico Land en lugar de GameObject.
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param game El mundo del juego
     * @return Una instancia de Land, o null si no es un Land
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
    @Override
    public Land parse(String[] objWords, GameWorld game) throws ObjectParseException {
        // Parsear elementos comunes (posición + validación de tipo)
        Position pos = parseCommon(objWords, "LAND", "L");
        if (pos == null) {
            return null; // No es un Land
        }
        
        // Validar que no hay argumentos extra
        validateMaxArgs(objWords, 2);
        
        return new Land(game, pos);
    }

    // Serializacion del objeto
    @Override
    public String serialize() {
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        return "(" + row + "," + col + ") Land";
    }

}
