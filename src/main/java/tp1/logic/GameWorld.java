package tp1.logic;

public interface GameWorld {

    GameObjectContainer getGameObjects();
    void addPoints(int points);
    void marioDies();
    void marioExited();
    void doInteractionsFrom(GameItem item);
}
