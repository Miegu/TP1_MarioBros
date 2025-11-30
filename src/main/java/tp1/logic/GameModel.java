package tp1.logic;


import tp1.exceptions.GameLoadException;
import tp1.exceptions.GameModelException;
import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.GameObject;

/**
 * Interfaz que define el contrato para las operaciones del modelo del juego.
 * Responsable de la GESTIÓN DE OBJETOS del juego.
 * Usado por el controlador para interactuar con la lógica del juego.
 */
public interface GameModel{
    
    // ===== GESTIÓN DE OBJETOS DEL JUEGO =====
    void addObject(GameObject object) throws OffBoardException;
    boolean removeObjectAt(Position pos);
    GameObject getObjectAt(Position pos);
    
    // ===== CONTROL DEL FLUJO DEL JUEGO =====
    boolean isFinished();
    void update();
    void reset();
    void reset(int level);
    void exit();
    
    // ===== ACCIONES DE MARIO =====
    void addAction(Action action);

    void load(String fileName) throws GameLoadException;
    void save(String fileName) throws GameModelException;
}
