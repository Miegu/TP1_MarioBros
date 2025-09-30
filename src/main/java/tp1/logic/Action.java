package tp1.logic;

/**
 * Represents the allowed actions in the game
 *
 */
public enum Action {
	LEFT(-1,0), RIGHT(1,0), DOWN(0,1), UP(0,-1), STOP(0,0);
	
	private int x;
	private int y;
	
	private Action(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	//TODO fill your code

	public static Action parse(String text) {
        if (text == null) return null;

        String upperText = text.toUpperCase().trim();

            return switch (upperText) {
                case "LEFT", "L", "l" -> LEFT;
                case "RIGHT", "R", "r" -> RIGHT;
                case "UP", "U", "u" -> UP;
                case "DOWN", "D", "d" -> DOWN;
                case "STOP", "S" ,"s" -> STOP;
                default -> null;
            };
    }
	
}
