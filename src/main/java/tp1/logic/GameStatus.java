package tp1.logic;

public interface GameStatus {
    boolean playerWins();
    boolean playerLoses();
    int remainingTime();
    int points();
    int numLives();
    String positionToString(int col, int row);
}
