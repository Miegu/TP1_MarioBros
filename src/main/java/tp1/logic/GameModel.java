package tp1.logic;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.GameModelException;
import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.GameObject;

/**
 * Interfaz que define el contrato para el modelo del juego.
 * 
 * El modelo es responsable de:
 * - Gestionar el estado del juego (puntos, vidas, tiempo)
 * - Controlar los objetos en el tablero
 * - Procesar acciones de Mario
 * - Mantener las reglas del juego
 * - Persistencia (guardar/cargar)
 * 
 * La interfaz permite implementaciones diferentes (simulador, modo creativo, etc.)
 * 
 */
public interface GameModel {

    // ===== CONSULTAS DE ESTADO =====
    
    /**
     * Verifica si el juego ha terminado (victoria, derrota o salida).
     * 
     * @return true si el juego está terminado
     */
    boolean isFinished();

    // ===== OPERACIONES DEL JUEGO =====
    
    /**
     * Procesa un turno del juego.
     * 
     * Responsabilidades en orden:
     * 1. Reducir tiempo disponible
     * 2. Actualizar posiciones de todos los objetos
     * 3. Detectar colisiones y interacciones
     * 4. Verificar condiciones de fin de juego
     */
    void update();
    
    /**
     * Reinicia el juego manteniendo el nivel actual.
     * Restaura puntuación a 0 y vidas a 3.
     */
    void reset();
    
    /**
     * Reinicia el juego en un nivel específico.
     * 
     * @param level Nivel a cargar (-1=creativo, 0=fácil, 1=normal, 2=difícil)
     */
    void reset(int level);
    
    /**
     * Marca el juego como finalizado y sale de forma controlada.
     */
    void exit();
    
    // ===== ACCIONES DE MARIO =====
    
    /**
     * Añade una acción a la lista de acciones pendientes de Mario.
     * 
     * Las acciones se procesarán en el siguiente update().
     * 
     * @param action Acción a ejecutar (LEFT, RIGHT, UP, DOWN, STOP)
     */
    void addAction(Action action);

    // ===== GESTIÓN DE OBJETOS DEL JUEGO =====

    /**
     * Añade un objeto al tablero del juego.
     * 
     * @param object Objeto a añadir
     * @throws OffBoardException Si la posición está fuera del tablero
     */
    void addObject(GameObject object) throws OffBoardException;
    
    /**
     * Elimina el objeto ubicado en la posición especificada.
     * 
     * @param pos Posición del objeto a eliminar
     * @return true si se eliminó algo, false si no había objeto
     */
    boolean removeObjectAt(Position pos);
    
    /**
     * Obtiene el objeto en la posición especificada.
     * 
     * @param pos Posición a consultar
     * @return Objeto en esa posición, o null si no existe
     */
    GameObject getObjectAt(Position pos);

    // ===== GESTION aRCHIVOS =====
    
    /**
     * Guarda el estado actual del juego en un archivo.
     * 
     * Formato:
     * - Línea 1: tiempo monedas vidas
     * - Línea 2+: objetos serializados
     * 
     * @param fileName Nombre del archivo de guardado
     * @throws GameModelException Si hay error al escribir el archivo
     */
    void save(String fileName) throws GameModelException;
    
    /**
     * Carga el estado del juego desde un archivo guardado anteriormente.
     * 
     * @param fileName Nombre del archivo a cargar
     * @throws GameLoadException Si hay error al leer el archivo o formato inválido
     */
    void load(String fileName) throws GameLoadException;
}
