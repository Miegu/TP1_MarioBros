package tp1.exceptions;

/**
 * Excepción para errores al parsear posiciones desde entrada de usuario o archivo.
 * 
 * Se lanza cuando:
 * - Formato de posición incorrecto (ej: no "x,y")
 * - Coordenadas no son números válidos
 * - Números son decimales cuando deben ser enteros
 * - Separador incorrecto (ej: espacio en lugar de coma)
 * 
 * Ejemplos de casos:
 * ```java
 * // Formato incorrecto
 * if (!positionStr.contains(",")) {
 *     throw new PositionParseException(
 *         "Invalid position format: " + positionStr + 
 *         " (expected: x,y)"
 *     );
 * }
 * 
 * // Números inválidos
 * try {
 *     int x = Integer.parseInt(parts[0]);
 *     int y = Integer.parseInt(parts[1]);
 * } catch (NumberFormatException e) {
 *     throw new PositionParseException(
 *         "Position numbers must be integers: " + positionStr, e
 *     );
 * }
 * ```
 * 
 */
public class PositionParseException extends GameModelException{
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error
     *        (ej: "Invalid position format: 10 15")
     */
    public PositionParseException(String message){
        super(message);
    }

    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error de parsing
     * @param cause Excepción original (ej: NumberFormatException)
     */
    public PositionParseException(String message, Throwable cause){
        super(message, cause);
    }
}
