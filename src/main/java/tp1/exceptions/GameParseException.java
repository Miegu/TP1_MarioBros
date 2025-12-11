package tp1.exceptions;

/**
 * Excepción para errores al parsear el estado del juego desde un archivo.
 * 
 * Se lanza cuando:
 * - Formato de archivo inválido
 * - Línea del estado del juego tiene formato incorrecto
 * - Valores esperados no están presentes
 * - Encoding de caracteres incorrectos
 * 
 * Jerarquía:
 * - GameParseException extends GameModelException
 *   - ActionParseException (parsing de acciones)
 * 
 * Ejemplo:
 * ```java
 * try {
 *     String[] parts = line.split(",");
 *     // parsing...
 * } catch (NumberFormatException e) {
 *     throw new GameParseException(
 *         "Invalid game state format: " + line, e
 *     );
 * }
 * ```
 */
public class GameParseException extends GameModelException {
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error de parsing
     */
    public GameParseException(String message){
        super(message);
    }
    
    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error de parsing
     * @param cause Excepción original (ej: NumberFormatException)
     */
    public GameParseException(String message, Throwable cause){
        super(message, cause);
    }
}
