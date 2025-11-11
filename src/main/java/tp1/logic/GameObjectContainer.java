package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.view.Messages;

public class GameObjectContainer {

    private List<GameObject> allObjects;
    private ExitDoor exitDoor;
    private Mario mario;

    public GameObjectContainer() {
        this.allObjects = new ArrayList<>();
        this.exitDoor = null;
        this.mario = null;
    }

    // Métodos genéricos de gestión
    public void add(GameObject obj) {
        if (obj instanceof Mario) {
            mario = (Mario) obj;
        } else if (obj instanceof ExitDoor) {
            exitDoor = (ExitDoor) obj;
        }
        allObjects.add(obj);
    }
    public void add(Land obj) {
        add((GameObject)obj);
    }

    public void add(Goomba obj) {
        add((GameObject)obj);
    }

    public void add(ExitDoor obj) {
        add((GameObject)obj);
    }

    public void add(Mario obj) {
        add((GameObject)obj);
    }

     // Método de borrado por posición
    public boolean removeObjectAt(Position pos) {
        return allObjects.removeIf(o -> o.isInPosition(pos));
    }

    public GameObject getObjectAt(Position pos) {
        for (GameObject obj : allObjects) if (obj.isInPosition(pos)) return obj;
        return null;
    }

    public void update() {
        // 1. PRIMERO Mario (orden importante)
        if (mario != null && mario.isAlive())
            mario.update();
        checkMarioInExit();

        // Demás objetos (Goomba, otros, menos Mario/META)
        for (GameObject obj : new ArrayList<>(allObjects)) {
            if (!(obj instanceof Mario) && !(obj instanceof ExitDoor) && obj.isAlive())
                obj.update();
        }

        doInteractionsFrom(mario);
        cleanupDeadObjects();
    }

    public boolean isSolid(Position position) {
        for (GameObject obj : allObjects)
            if (obj.isAlive() && obj.isSolid() && obj.isInPosition(position))
                return true;
        return false;
    }

    public void checkMarioInExit() {
        if (mario != null && exitDoor != null && mario.isInPosition(exitDoor.getPosition())) {
            //Mario ha llegado a la puerta de salida
            mario.interactWith(exitDoor);
        }
    }

    private void cleanupDeadObjects() {
        // Elimina solo objetos no esenciales y no vivos (se mantiene Mario/door siempre)
        allObjects.removeIf(obj ->
            !(obj instanceof Mario) && !(obj instanceof ExitDoor) && !obj.isAlive()
        );
    }
    public void doInteractionsFrom(Mario mario) {
        if (mario == null) return;
        for (GameObject obj : new ArrayList<>(allObjects)) {
            if (obj != mario && mario.interactWith(obj)) break;
        }
    }


    public String positionToString(Position position) {
        StringBuilder sb = new StringBuilder();
        for (GameObject obj : allObjects)
            if (obj.isAlive() && obj.isInPosition(position))
                sb.append(obj.getIcon());
        return sb.length() == 0 ? Messages.EMPTY : sb.toString();
    }
}
