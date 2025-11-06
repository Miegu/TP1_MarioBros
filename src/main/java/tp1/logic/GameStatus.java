package tp1.logic;

public interface GameStatus {

	public String positionToString(int col, int row);
	void update();
    boolean isFinished();
    void reset();
	void reset(int level);
	void exit();
	int remainingTime();
    int points(); 
    int numLives();
    boolean playerWins();
    boolean playerLoses();
}
