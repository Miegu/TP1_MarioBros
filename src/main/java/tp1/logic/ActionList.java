package tp1.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona las acciones que el jugador le añade a Mario
 *
 */
public class ActionList {

    private List<Action> actions;
    private static final int MAX_ACTIONS = 4;

    public ActionList() {
        this.actions = new ArrayList<>();
    }

    /**
     * Añade una accion, respetando las restricciones -Máximo 4 acciones por
     * turno -LEFT/RIGHT si aparecen seguidas se ignora la segunda -UP/DOWN pues
     * lo mismo
     */
    public void addAction(Action action) {

        // Verificar si ya hay 4 acciones
        if (countAction(action) >= MAX_ACTIONS) {
            return;
        }

        // Verificar conflictos
        if (hasConflictingAction(action)) {
            return;
        }

        actions.add(action);
    }

    private int countAction(Action actionType) {
        int count = 0;
        for (Action a : actions) {
            if (a == actionType) {
                count++;
            }
        }
        return count;
    }

    /*
      * Verifica si la nueva accion es conflictiva
     */
    private boolean hasConflictingAction(Action newAction) {
        for (Action existing : actions) {
            //Conflicto LEFT/RIGHT
            if ((newAction == Action.LEFT && existing == Action.RIGHT)
                    || (newAction == Action.RIGHT && existing == Action.LEFT)) {
                return true;
            }
            //Conflicto UP/DOWN
            if ((newAction == Action.UP && existing == Action.DOWN)
                    || (newAction == Action.DOWN && existing == Action.UP)) {
                return true;
            }
        }
        return false;
    }

    /* 
    *Obtiene una copia de las acciones a ejecutar
     */
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    /**
     * Verifica si hay acciones pendientes
     */
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public int size() {
        return actions.size();
    }

    /**
     * Limpia la lista IMPORTANTE
     */
    public void clear() {
        actions.clear();
    }
}