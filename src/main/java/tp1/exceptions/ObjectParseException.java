package tp1.exceptions;

/**
 * Excepción para errores al parsear descripciones de objetos del tablero.
 * 
 * Se lanza cuando:
 * - Objeto desconocido (ej: "DINO" en lugar de "GOOMBA")
 * - Formato incorrecto (ej: faltan parámetros)
 * - Posición inválida para ese objeto
 * - Parámetros incompatibles (ej: Mario con tamaño "GIANT")
 * - Dirección inválida para objetos móviles
 * 
 * Formato esperado:
 * - "(x,y) ObjectName [direction [size]]"
 * - "(10,5) Mario RIGHT SMALL"
 * - "(3,2) Goomba LEFT"
 * - "(8,8) Mushroom"
 * 
 * Ejemplos de casos:
 * ```java
 * // Objeto desconocido
 * if (!isKnownObject(objectName)) {
 *     throw new ObjectParseException(
 *         "Unknown object: " + objectName
 *     );
 * }
 * 
 * // Parámetros incompatibles
 * if (objectName.equals("BOX") && hasDirection) {
 *     throw new ObjectParseException(
 *         "Box cannot have a direction"
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - ObjectParseException extends GameModelException
 * 
 */
public class ObjectParseException extends GameModelException{
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error
     *        (ej: "Unknown object: DINO")
     */
    public ObjectParseException(String message){
        super(message);
    }
    
    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error de parsing de objeto
     * @param cause Excepción original que causó este error
     */
    public ObjectParseException(String message, Throwable cause){
        super(message, cause);
    }
}
