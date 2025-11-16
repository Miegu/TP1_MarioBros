package tp1.logic;

/**
 * Interfaz para la vista.
 * Proporciona información de solo lectura sobre el estado del juego.
 */
public interface GameStatus {

	public String positionToString(int col, int row);
	int remainingTime();
    int points(); 
    int numLives();
    boolean playerWins();
    boolean playerLoses();
}
