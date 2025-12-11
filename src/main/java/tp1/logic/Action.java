package tp1.logic;

import tp1.exceptions.ActionParseException;
import tp1.view.Messages;
/**
 * Enum que representa las acciones posibles de Mario.
 *
 */
public enum Action {
    LEFT(-1, 0), RIGHT(1, 0), DOWN(0, 1), UP(0, -1), STOP(0, 0);

    // Vector de movimiento
    private int x;
    private int y;

    private Action(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Parsea una cadena a una acción.
     * 
     * Soporta múltiples formatos:
     * - Nombres completos: LEFT, RIGHT, UP, DOWN, STOP (case-insensitive)
     * - Abreviaturas: L, R, U, D, S (case-insensitive)
     * 
     * Ejemplos:
     * - "LEFT" -> LEFT
     * - "left" -> LEFT
     * - "L" -> LEFT
     * - "l" -> LEFT
     * 
     * @param text Cadena a parsear
     * @return Acción parseada
     * @throws ActionParseException Si cadena es null, vacía o no reconocida
     */
    public static Action parse(String text) throws ActionParseException {
        if (text == null || text.trim().isEmpty()) {
            throw new ActionParseException(Messages.ERROR_ACTION_NULL_OR_EMPTY);
        }

        String upperText = text.trim().toUpperCase();

        return switch (upperText) {
            // Movimientos horizontales
            case "LEFT", "L" -> LEFT;
            case "RIGHT", "R" -> RIGHT;
            
            // Movimientos verticales
            case "UP", "U" -> UP;
            case "DOWN", "D" -> DOWN;
            
            // Sin movimiento
            case "STOP", "S" -> STOP;
            
            // Desconocida
            default -> throw new ActionParseException(
                Messages.ERROR_UNKNOWN_ACTION.formatted(text)
            );
        };
    }

}
