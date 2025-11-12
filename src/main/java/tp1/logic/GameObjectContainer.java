package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameObject;
import tp1.view.Messages;

public class GameObjectContainer {

    private List<GameObject> objects;

    public GameObjectContainer() {
        this.objects = new ArrayList<>();
    }

    // Métodos genéricos de gestión
    public void add(GameObject obj) {
        objects.add(obj);
    }

    // Método de borrado por posición
    public boolean removeObjectAt(Position pos) {
        return objects.removeIf(obj -> obj.isInPosition(pos));
    }

    public GameObject getObjectAt(Position pos) {
        for (GameObject obj : objects) {
            if (obj.isInPosition(pos)) {
                return obj;
            }
        }
        return null;
    }

    public void update() {
        // Actualizar todos los objetos (polimórfico)

        for (GameObject obj : new ArrayList<>(objects)) {
            if (obj.isAlive() && obj.shouldUpdateInLoop()) {
                obj.update();
            }
        }

        objects.removeIf(obj -> !obj.isAlive() && obj.canBeRemoved());
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

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);  // Defensive copy
    }
}
