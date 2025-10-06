package tp1.logic;

import java.util.ArrayList;
import java.util.List;
/**
 * Gestiona las acciones que el jugador le añade a Mario
 *
 */
public class ActionList {
    private List<Action> actions;
    private static final int MAX_ACTIONS = 5;

    public ActionList(){
        this.actions = new ArrayList<>();
    }

    /**
     * Añade una accion, respetando las restricciones
     * -Máximo 5 acciones por turno
     * -LEFT/RIGHT si aparecen seguidas se ignora la segunda
     * -UP/DOWN pues lo mismo
     */

     public void addAction(Action action){
        if(actions.size() >= MAX_ACTIONS){
            return; //Ignoras las adicionales, pero no da error
        }

        if(hasConflictingAction(action)){
            return; //Ignora si hay conflicto;
        }

        actions.add(action); //Añade a la lista
     }

     /*
      * Verifica si la nueva accion es conflictiva
      */

      private boolean hasConflictingAction(Action newAction){
        for(Action existing : actions){
            //Conflicto LEFT/RIGHT
            if((newAction == Action.LEFT && existing == Action.RIGHT) ||
                (newAction == Action.RIGHT && existing == Action.LEFT)){
                return true;
            }
            //Conflicto UP/DOWN
            if((newAction == Action.UP && existing == Action.DOWN) ||
                (newAction == Action.DOWN && existing == Action.UP)){
                return true;
            }
        }
        return false;
      }

    /* 
    *Obtiene una copia de las acciones a ejecutar
     */     
      public List<Action> getActions(){
        return new ArrayList<>(actions);
      }
    /**
     * Verifica si hay acciones pendientes
    */
    public boolean isEmpty(){
        return actions.isEmpty();
    }

    public int size(){
        return actions.size();
    }

    /**
     * Limpia la lista IMPORTANTE
     */
      public void clear(){
        actions.clear();
      }
}
