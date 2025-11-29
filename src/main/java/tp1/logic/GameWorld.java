package tp1.logic;

import tp1.exceptions.OffBoardException;
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
    void addObject(GameObject object) throws OffBoardException;
    boolean removeObjectAt(Position pos);
    GameObject getObjectAt(Position pos);

    //Consulta sobre el estado del mundo
    boolean isSolid(Position pos);

    //Establece el Mario jugable
    void registerAsMain(GameObject mario);

    //Callbacks de eventos del juego
    void addScore(int points);
    void loseLife();
    void marioReachedExit();

    //Gestión de interacciones
    void doInteractionsFrom(GameItem item);
}