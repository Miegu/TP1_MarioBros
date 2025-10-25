package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.view.Messages;

public class GameObjectContainer {
    //TODO fill your code

    private List<Land> lands;
    private List<Goomba> goombas;
    private ExitDoor exitDoor;
    private Mario mario;

    public GameObjectContainer() {
        this.lands = new ArrayList<>();
        this.goombas = new ArrayList<>();
        this.exitDoor = null;
        this.mario = null;
    }

    public void add(Land land) {
        lands.add(land);
    }

    public void add(Goomba goomba) {
        goombas.add(goomba);
    }

    public void add(ExitDoor exitDoor) {
        this.exitDoor = exitDoor;
    }

    public void add(Mario mario) {
        this.mario = mario;
    }

    public void update() {
        // 1. PRIMERO Mario (orden importante según especificación)
        if (mario != null) {
            mario.update();
        }

        checkMarioInExit();
        // 2. DESPUÉS los Goombas
        for (Goomba goomba : goombas) {
            goomba.update();
        }
        doInteractionsFrom(mario);
        // 3. Eliminar Goombas muertos
        cleanupDeadGoombas();
    }

    public boolean isSolid(Position position) {
        //Comprueba si la posicion es la de algun Land
        for (Land land : lands) {
            if (land.isInPosition(position)) {
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

}
