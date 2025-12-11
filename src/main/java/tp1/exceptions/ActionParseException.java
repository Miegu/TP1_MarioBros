package tp1.exceptions;

/**
 * Excepción para errores al parsear acciones del usuario.
 * 
 * Se lanza cuando:
 * - Acción desconocida o inválida
 * - Número incorrecto de parámetros
 * - Dirección inválida para acción de movimiento
 * - Formato de acción incorrecto
 * 
 * Ejemplos de casos:
 * ```java
 * // Acción desconocida
 * if (!isValidAction(actionStr)) {
 *     throw new ActionParseException(
 *         "Unknown action: " + actionStr
 *     );
 * }
 * 
 * // Parámetros incorrectos
 * if (params.length != expectedCount) {
 *     throw new ActionParseException(
 *         "Action requires " + expectedCount + " parameters"
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - ActionParseException extends GameParseException
 *   └── extends GameModelException
 */
public class ActionParseException extends GameParseException{
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error (ej: "Unknown action: JUMP")
     */
    public ActionParseException(String message){
        super(message);
    }
    
    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error de parsing de acción
     * @param cause Excepción original que causó este error
     */
    public ActionParseException(String message, Throwable cause){
        super(message, cause);
    }
}
