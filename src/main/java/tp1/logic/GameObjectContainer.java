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
        objects.removeIf(obj -> !obj.isAlive() && obj.canBeRemoved());
    }

    public void doInteraction(GameItem item){
        for(GameObject obj : new ArrayList<>(objects)){
            if(obj.isAlive() && item.isAlive()){
                item.interactWith(obj);
                obj.interactWith(item);
            }
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

    public String positionToString(Position position) {
        StringBuilder sb = new StringBuilder();
        for (GameObject obj : objects) {
            if (obj.isAlive() && obj.isInPosition(position)) {
                sb.append(obj.getIcon());
            }
        }
        return sb.length() == 0 ? Messages.EMPTY : sb.toString();
    }
    //Acceso seguro a la lista
    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }
}
