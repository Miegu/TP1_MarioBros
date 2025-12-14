package tp1.logic;

import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.PlayableObject;

/**
 * Interfaz para callbacks desde los objetos del juego.
 * Proporciona métodos para que los objetos INTERACTÚEN con el mundo del juego.
 * Responsable de INTERACCIONES Y CONSULTAS sobre el estado del mundo.
 */
public interface GameWorld{
    // ===== DIMENSIONES DEL MUNDO =====
    int getRows();
    int getCols();
    boolean isInside(Position pos);

    // ===== CONSULTA SOBRE EL ESTADO DEL MUNDO =====
    boolean isSolid(Position pos);

    // ===== REGISTRO DE MARIO JUGABLE =====
    void registerAsMain(PlayableObject player);

    // ===== CALLBACKS DE EVENTOS DEL JUEGO =====
    void addScore(int points);
    void loseLife();
    void marioReachedExit();

    // ===== GESTIÓN DE INTERACCIONES =====
    void doInteractionsFrom(GameItem item);
    void addObject(GameObject object) throws OffBoardException;
}
