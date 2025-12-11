package tp1.exceptions;

/**
 * Excepción cuando una posición está fuera de los límites del tablero.
 * 
 * Se lanza cuando:
 * - Coordenada x < 0 o x >= DIM_X (30)
 * - Coordenada y < 0 o y >= DIM_Y (15)
 * - Un objeto se intenta colocar fuera del tablero
 * 
 * Límites del tablero:
 * - Ancho: 0 a 29 (DIM_X = 30)
 * - Alto: 0 a 14 (DIM_Y = 15)
 * 
 * Ejemplos de casos:
 * ```java
 * // Crear posición fuera del tablero
 * if (x < 0 || x >= 30 || y < 0 || y >= 15) {
 *     throw new OffBoardException(
 *         "Position (" + x + "," + y + 
 *         ") is outside board limits [0-29, 0-14]"
 *     );
 * }
 * 
 * // Objetos con posición inválida
 * if (!isValidBoardPosition(x, y)) {
 *     throw new OffBoardException(
 *         "Cannot place object at (" + x + "," + y + 
 *         "): position is off-board"
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - OffBoardException extends GameModelException
 */
public class OffBoardException extends GameModelException{
    
    /** Límite mínimo de coordenadas (0) */
    public static final int MIN_COORDINATE = 0;
    
    /** Ancho del tablero (30) */
    public static final int BOARD_WIDTH = 30;
    
    /** Alto del tablero (15) */
    public static final int BOARD_HEIGHT = 15;
    
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error
     *        (ej: "Position (35, 20) is outside board limits")
     */
    public OffBoardException(String message) {
        super(message);
    }

    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error
     * @param cause Excepción original
     */
    public OffBoardException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Verifica si una posición está dentro del tablero.
     * 
     * @param x Coordenada x
     * @param y Coordenada y
     * @return true si la posición está dentro del tablero
     */
    public static boolean isValidPosition(int x, int y) {
        return x >= MIN_COORDINATE && x < BOARD_WIDTH &&
               y >= MIN_COORDINATE && y < BOARD_HEIGHT;
    }
}
