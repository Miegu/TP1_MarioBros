package tp1.exceptions;

/**
 * Excepción base para todos los errores del modelo del juego.
 * 
 * Representa errores en:
 * - Parsing del estado del juego
 * - Validación de posiciones
 * - Validación de objetos del tablero
 * - Lógica del modelo
 * 
 * Jerarquía:
 * - GameModelException (padre de todas las excepciones del modelo)
 *   - GameParseException (parsing de estado)
 *     - ActionParseException (parsing de acciones)
 *   - PositionParseException (posiciones inválidas)
 *   - ObjectParseException (objetos inválidos)
 *   - OffBoardException (fuera del tablero)
 * 
 * Ejemplo de uso:
 * ```java
 * try {
 *     Position pos = new Position(x, y);
 * } catch (GameModelException e) {
 *     System.err.println("Error del modelo: " + e.getMessage());
 * }
 * ```
 */
public class GameModelException extends Exception{
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error
     */
    public GameModelException(String message){
        super(message);
    }

    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * Útil para envolver excepciones de bajo nivel con contexto de negocio.
     * 
     * @param message Descripción del error de negocio
     * @param cause Excepción original que causó este error
     */
    public GameModelException(String message, Throwable cause){
        super(message, cause);
    }
}
