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

	public GameObjectContainer(){
		this.lands = new ArrayList<>();
		this.goombas = new ArrayList<>();
		this.exitDoor = null;
		this.mario = null;
	}

	
	public void add(Land land){
		lands.add(land);
	}

	public void add(Goomba goomba){
		goombas.add(goomba);
	}
	public void add(ExitDoor exitDoor){
		this.exitDoor = exitDoor;
	}
	public void add(Mario mario){
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
        goombas.removeIf(goomba -> !goomba.estaVivo());
    }
	public boolean isSolid(Position position){
		//Comprueba si la posicion es la de algun Land
		for(Land land : lands){
			if(land.isInPosition(position)){
				return true;
			}
		}
		return false;
	}
	public void checkMarioInExit(){
		if(mario != null && exitDoor != null && mario.isInPosition(exitDoor.getPosition())){
			//Mario ha llegado a la puerta de salida
			mario.interactWith(exitDoor);
		}
	}	

	public void doInteractionsFrom(Mario mario){
		if(mario == null) return; //Si no hay Mario no hace nada

		//Comprueba interacciones con Goombas
		for(Goomba goomba : goombas){
			mario.interactWith(goomba);
		}
	}

	public String positionToString(Position position){
		//Comprueba si la posicion es la de Mario
		if(mario != null && mario.isInPosition(position)){
			return mario.getIcon();
		}
		//Comprueba si la posicion es la de la puerta de salida
		if(exitDoor != null && exitDoor.isInPosition(position)){
			return exitDoor.getIcon();
		}
		//Comprueba si la posicion es la de algun Goomba
		for(Goomba goomba : goombas){
			if(goomba.isInPosition(position)){
				return goomba.getIcon();
			}
		}
		//Comprueba si la posicion es la de alguna tierra
		for(Land land : lands){
			if(land.isInPosition(position)){
				return land.getIcon();
			}
		}
		return Messages.EMPTY; //Si no hay nada en esa posicion, devuelve un espacio en blanco
	}
	
}
