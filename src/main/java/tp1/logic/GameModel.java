package tp1.logic;

public interface GameModel {
    boolean isFinished();
    void update();
    void reset();
    void reset(int level);
    void exit();
}
