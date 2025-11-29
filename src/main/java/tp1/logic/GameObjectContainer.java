package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;
import tp1.view.Messages;

public class GameObjectContainer {

    private List<GameObject> objects;

    public GameObjectContainer() {
        this.objects = new ArrayList<>();
    }

    // Métodos para añadir objetos al contenedor
    // Si se añade antes se actualiza antes
    public void add(GameObject obj) {
        objects.add(obj);
    }

    // Borrar objeto en la posicion
    public boolean removeObjectAt(Position pos) {
        return objects.removeIf(obj -> obj.isInPosition(pos));
    }
    //Obtenes objeto en la posicion dada
    public GameObject getObjectAt(Position pos) {
        for (GameObject obj : objects) {
            if (obj.isInPosition(pos)) {
                return obj;
            }
        }
        return null;
    }
    // Actualizar todos los objetos vivos en orden de inserción
    public void update() {
        //Actualizar
        for (GameObject obj : new ArrayList<>(objects)) {
            if (obj.isAlive()) {
                obj.update();
            }
        }
        //Limpiar objetos muertos que puedan ser removidos
        objects.removeIf(obj -> !obj.isAlive());
    }

    public void doInteraction(GameItem item){
        // Solo procesar si ambos están vivos y no son el mismo objeto
        for(GameObject obj : new ArrayList<>(objects)){
        if(obj.isAlive() && item.isAlive() && obj != item){
            // Primera llamada
            boolean interacted1 = item.interactWith(obj);

            // Si hubo interacción exitosa, salir del bucle
            if(interacted1){
                break;
            }
            if(obj.isAlive() && item.isAlive()){
                boolean interacted2 = obj.interactWith(item);
                
                // Si hubo interacción exitosa, salir del bucle
                if(interacted2){
                    break;
                }
            }
        }
        doInteractionsFrom(mario);
        // 3. Eliminar Goombas muertos
        cleanupDeadGoombas();
    }
}

    public boolean isSolid(Position position) {
        for (GameObject obj : objects) {
            if (obj.isAlive() && obj.isSolid() && obj.isInPosition(position)) {
                return true;
            }
        }
        return false;
    }

    public void checkMarioInExit() {
        if (mario != null && exitDoor != null && mario.isInPosition(exitDoor.getPosition())) {
            //Mario ha llegado a la puerta de salida
            mario.interactWith(exitDoor);
        }
    }
    private void cleanupDeadGoombas() {
        goombas.removeIf(goomba -> !goomba.estaVivo());
    }

    public void doInteractionsFrom(Mario mario) {
    if (mario == null) {
        return;
    }
    
    for (Goomba goomba : goombas) {
        if (mario.interactWith(goomba)) {
            break;
        }
        
    }
    }


    public String positionToString(Position position) {
        StringBuilder sb = new StringBuilder();
        //Comprueba si la posicion es la de Mario
        if (mario != null && mario.isInPosition(position)) {
            sb.append(mario.getIcon());
        }
        //Comprueba si la posicion es la de la puerta de salida
        if (exitDoor != null && exitDoor.isInPosition(position)) {
            sb.append(exitDoor.getIcon());
        }
        //Comprueba si la posicion es la de algun Goomba
        for (Goomba goomba : goombas) {
            if (goomba.isInPosition(position)) {
                sb.append(goomba.getIcon());
            }
        }

        //Comprueba si la posicion es la de alguna tierra
        for (Land land : lands) {
            if (land.isInPosition(position)) {
                sb.append(land.getIcon());
            }
        }

        if (sb.length() == 0) {
         return Messages.EMPTY; //Si no hay nada en esa posicion, devuelve un espacio en blanco
        } 
        // Devolver todos los iconos concatenados
        return sb.toString();
    }
    //Acceso seguro a la lista
    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }
}