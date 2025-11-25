package tp1.logic;

import tp1.exceptions.ActionParseException;
import tp1.view.Messages;
/**
 * Represents the allowed actions in the game
 *
 */
public enum Action {
    LEFT(-1, 0), RIGHT(1, 0), DOWN(0, 1), UP(0, -1), STOP(0, 0);

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

    public static Action parse(String text) throws ActionParseException{
        if (text == null || text.trim().isEmpty()) {
            throw new ActionParseException(Messages.ERROR_ACTION_NULL_OR_EMPTY);
        }
        
        String upperText = text.trim().toUpperCase();
        
        return switch (upperText) {
            case "LEFT", "L" ->
                LEFT;
            case "RIGHT", "R" ->
                RIGHT;
            case "UP", "U" ->
                UP;
            case "DOWN", "D" ->
                DOWN;
            case "STOP", "S" ->
                STOP;
            default -> throw new ActionParseException(
                Messages.ERROR_UNKNOWN_ACTION.formatted(text));
        };
    }

}
