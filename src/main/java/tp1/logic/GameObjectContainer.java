package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;
import tp1.view.Messages;

/**
 * Contenedor centralizado para todos los objetos del juego.
 * 
 * Responsabilidades:
 * - Almacenar y actualizar GameObjects
 * - Búsqueda de objetos por posición
 * - Validación de solidez en una posición
 * - Renderizado de objetos en una posición
 * - Gestión de interacciones entre objetos
 * 
 * Patrón:
 * - Single List of Objects: todos los objetos en una lista
 * - Iteración segura: copia la lista antes de iterar
 * - Limpieza automática: remueve objetos muertos que pueden serlo
 * 
 */
public class GameObjectContainer {

    private List<GameObject> objects;

    /**
     * Crea un contenedor vacío.
     */
    public GameObjectContainer() {
        this.objects = new ArrayList<>();
    }

    // ===== BÁSICO =====

    /**
     * Añade un objeto al contenedor.
     * Los objetos se añaden al final de la lista.
     * El orden de inserción determina el orden de actualización.
     * 
     * @param obj Objeto a añadir (no null)
     */
    public void add(GameObject obj) {
        objects.add(obj);
    }

    /**
     * Elimina el objeto en la posición especificada.
     * 
     * Si hay múltiples objetos en esa posición, elimina el primero encontrado.
     * Usa removeIf para eliminar de forma segura durante iteración.
     * 
     * @param pos Posición del objeto a eliminar
     * @return true si se eliminó algo, false si no había objeto
     */
    public boolean removeObjectAt(Position pos) {
        return objects.removeIf(obj -> obj.isInPosition(pos));
    }

    /**
     * Obtiene el objeto en la posición especificada.
     * 
     * Si hay múltiples objetos en esa posición, devuelve el primero.
     * 
     * @param pos Posición a consultar
     * @return Objeto en esa posición, o null si no existe
     */
    public GameObject getObjectAt(Position pos) {
        for (GameObject obj : objects) {
            if (obj.isInPosition(pos)) {
                return obj;
            }
        }
        return null;
    }

    // ===== ACTUALIZACIÓN =====

    /**
     * Actualiza todos los objetos vivos en orden de inserción.
     * 
     * Proceso en dos fases:
     * 1. Actualizar: Itera copia para permitir cambios estructurales
     * 2. Limpiar: Remueve objetos muertos que pueden serlo (canBeRemoved)
     * 
     * La iteración se hace sobre una copia para que update() pueda
     * agregar/remover objetos sin problemas.
     */
    public void update() {
        // Fase 1: Actualizar todos los objetos vivos
        updateAliveObjects();

        // Fase 2: Limpiar objetos muertos que pueden serlo
        removeDeadRemovableObjects();
    }

    /**
     * Actualiza todos los objetos vivos.
     * Itera sobre copia para permitir cambios estructurales.
     */
    private void updateAliveObjects() {
        List<GameObject> objectsCopy = new ArrayList<>(objects);

        for (GameObject obj : objectsCopy) {
            if (obj.isAlive()) {
                obj.update();
            }
        }
    }
    /**
     * Remueve objetos muertos que pueden ser removidos automáticamente.
     */
    private void removeDeadRemovableObjects() {
        objects.removeIf(obj -> !obj.isAlive() && obj.canBeRemoved());
    }

    // ===== INTERACCIONES =====

    /**
     * Procesa todas las interacciones del objeto especificado con otros.
     * 
     * Algoritmo:
     * 1. Iterar sobre copia para permitir cambios
     * 2. Para cada objeto vivo diferente al item:
     *    a. item interactúa con obj
     *    b. Si ambos siguen vivos, obj interactúa con item
     * 3. IMPORTANTE: Procesa TODAS las interacciones (sin breaks)
     * 
     * Esto permite que un projectil interactúe con múltiples objetos
     * en una misma posición (ej: disparo atraviesa múltiples enemigos).
     * 
     * @param item Objeto que inicia las interacciones
     */
    public void doInteraction(GameItem item) {
        List<GameObject> objectsCopy = new ArrayList<>(objects);
        for (GameObject obj : objectsCopy) {
            if (isValidInteractionTarget(obj, item)) {
                // Item interactúa con obj
                item.interactWith(obj);

                // Si ambos siguen vivos, obj interactúa con item
                if (obj.isAlive() && item.isAlive()) {
                    obj.interactWith(item);
                }
            }
        }
    }

    /**
     * Verifica si un objeto es un objetivo válido para interacción.
     * 
     * Condiciones:
     * - obj está vivo
     * - item está vivo
     * - son objetos diferentes
     * 
     * @param obj Objeto potencial objetivo
     * @param item Objeto que intenta interactuar
     * @return true si son válidos para interactuar
     */
    private boolean isValidInteractionTarget(GameObject obj, GameItem item) {
        return obj.isAlive() && item.isAlive() && obj != item;
    }

    // ===== CONSULTAS =====

    /**
     * Verifica si la posición es sólida (hay un objeto sólido).
     * 
     * Búsqueda: 
     * 1. Iterar objetos vivos
     * 2. Si es sólido e está en la posición, retorna true
     * 3. Si ninguno cumple, retorna false
     * 
     * @param pos Posición a consultar
     * @return true si hay un objeto sólido en esa posición
     */
    public boolean isSolid(Position pos) {
        for (GameObject obj : objects) {
            if (obj.isAlive() && obj.isSolid() && obj.isInPosition(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene la representación visual de una posición.
     * 
     * Renderizado:
     * 1. Iterar objetos vivos en la posición
     * 2. Concatenar sus iconos
     * 3. Si no hay ninguno, retorna EMPTY
     * 
     * Nota: Si hay múltiples objetos, se concatenan los iconos.
     * Esto permite layering visual (ej: Mario sobre plataforma).
     * 
     * @param pos Posición a renderizar
     * @return String con el icono/representación de esa posición
     */
    public String positionToString(Position pos) {
        StringBuilder sb = new StringBuilder();
        for (GameObject obj : objects) {
            if (obj.isAlive() && obj.isInPosition(pos)) {
                sb.append(obj.getIcon());
            }
        }
        return sb.length() == 0 ? Messages.EMPTY : sb.toString();
    }

    /**
     * Obtiene acceso seguro a la lista de objetos.
     * 
     * Retorna una COPIA para proteger la lista interna.
     * Modificar la lista devuelta no afecta el contenedor.
     * 
     * @return Nueva lista con copia de los objetos
     */
    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }
}
