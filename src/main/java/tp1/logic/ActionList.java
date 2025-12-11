package tp1.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la lista de acciones pendientes de Mario.
 *
 * Responsabilidades: 
 * - Almacenar acciones en orden de inserción 
 * - Validar restricciones (máx 4 acciones, conflictos de dirección) 
 * - Proporcionar acceso seguro a la lista (copia defensiva)
 *
 * Restricciones: 
 * 1. Máximo 4 acciones por turno 
 * 2. No permitir acciones conflictivas (LEFT+RIGHT, UP+DOWN) 
 * 3. Permitir múltiples acciones del mismo tipo
 *
 */
public class ActionList {

    private List<Action> actions;
    private static final int MAX_ACTIONS = 4;

    /**
     * Crea una lista de acciones vacía.
     */
    public ActionList() {
        this.actions = new ArrayList<>();
    }

    /**
     * Intenta añadir una acción a la lista.
     * 
     * Valida:
     * 1. No exceder MAX_ACTIONS del mismo tipo
     * 2. No crear conflictos de dirección (LEFT+RIGHT, UP+DOWN)
     * 
     * Si la validación falla, la acción se ignora silenciosamente.
     * 
     * @param action Acción a añadir (LEFT, RIGHT, UP, DOWN, STOP)
     */
    public void addAction(Action action) {
        // Verificar si ya hay demasiadas acciones del mismo tipo
        if (countAction(action) >= MAX_ACTIONS) {
            return; // Ignorar, límite alcanzado
        }

        // Verificar si hay conflictos con acciones existentes
        if (hasConflictingAction(action)) {
            return;
        }
        // Añadir la acción
        actions.add(action);
    }

    /**
     * Cuenta cuántas acciones del tipo especificado hay en la lista.
     * 
     * @param actionType Tipo de acción a contar
     * @return Número de acciones de ese tipo
     */
    private int countAction(Action actionType) {
        int count = 0;
        for (Action a : actions) {
            if (a == actionType) {
                count++;
            }
        }
        return count;
    }

    /**
     * Verifica si la nueva acción causaría un conflicto con las existentes.
     * 
     * Conflictos:
     * - LEFT + RIGHT son opuestos (inmoviliza)
     * - UP + DOWN son opuestos (inmoviliza)
     * - STOP no causa conflicto con nada
     * 
     * @param newAction Acción a verificar
     * @return true si causa conflicto, false si es compatible
     */
    private boolean hasConflictingAction(Action newAction) {
        for (Action existing : actions) {
            // Conflicto horizontal
            if (isHorizontalConflict(newAction, existing)) {
                return true;
            }

            // Conflicto vertical
            if (isVerticalConflict(newAction, existing)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si dos acciones tienen conflicto horizontal.
     * @param action1 Primera acción
     * @param action2 Segunda acción
     * @return true si son LEFT y RIGHT en cualquier orden
     */
    private boolean isHorizontalConflict(Action action1, Action action2) {
        return (action1 == Action.LEFT && action2 == Action.RIGHT)
            || (action1 == Action.RIGHT && action2 == Action.LEFT);
    }

    /**
     * Verifica si dos acciones tienen conflicto vertical.
     * @param action1 Primera acción
     * @param action2 Segunda acción
     * @return true si son UP y DOWN en cualquier orden
     */
    private boolean isVerticalConflict(Action action1, Action action2) {
        return (action1 == Action.UP && action2 == Action.DOWN)
            || (action1 == Action.DOWN && action2 == Action.UP);
    }

    /* 
    *Obtiene una copia de las acciones almacenadas.
    * 
    * Copia defensiva: modificar la lista devuelta no afecta la interna.
    * 
    * @return Nueva lista con copia de las acciones
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
     * Limpia la lista IMPORTANTE después de procesar las acciones en un turno.
     */
    public void clear() {
        actions.clear();
    }
}
