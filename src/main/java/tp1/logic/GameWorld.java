package tp1.logic;

import tp1.logic.gameobjects.GameObject;
public interface GameWorld {
    int getRows();
    int getCols();
    boolean isInside(Position pos);


    //Gestion de los objetos del juego
    boolean addObject(GameObject object);
    boolean removeObjectAt(Position pos);
    GameObject getObjectAt(Position pos);

    //Interacciones
    boolean isSolid(Position pos);
    void addScore(int points);
    void loseLife();
    void resetLevel(int levelId);
    void marioReachedExit();

    void updateAll();
}
