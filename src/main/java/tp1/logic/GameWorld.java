package tp1.logic;

import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;

/**
 * Interfaz para callbacks desde los objetos del juego.
 * Proporciona métodos para que los objetos interactúen con el mundo del juego.
 */
public interface GameWorld{
    int getRows();
    int getCols();
    boolean isInside(Position pos);

    //Gestion de los objetos del juego
    boolean addObject(GameObject object);
    boolean removeObjectAt(Position pos);
    GameObject getObjectAt(Position pos);

    //Consulta sobre el estado del mundo
    boolean isSolid(Position pos);

    //Callbacks de eventos del juego
    void addScore(int points);
    void loseLife();
    void marioReachedExit();

    //Gestión de interacciones
    void doInteractionsFrom(GameItem item);
}
