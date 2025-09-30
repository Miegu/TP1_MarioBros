package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;


public class GameObjectContainer {
	//TODO fill your code
	private List<GameObject> objects = new ArrayList<>();
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

	 public void add(GameObject obj) {
        objects.add(obj);
    }
	public void addLand(Land land){
		lands.add(land);
	}

	public void addGoomba(Goomba goomba){
		goombas.add(goomba);
	}
	public void setExitDoor(ExitDoor exitDoor){
		this.exitDoor = exitDoor;
	}
	public void setMario(Mario mario){
		this.mario = mario;
	}

	public String positionToString(Position position){
		//Comprueba si la posicion es la de Mario
		if(mario != null && mario.isInPosition(position)){
			return String.valueOf(mario.getIcon());
		}
		//Comprueba si la posicion es la de la puerta de salida
		if(exitDoor != null && exitDoor.isInPosition(position)){
			return String.valueOf(exitDoor.getIcon());
		}
		//Comprueba si la posicion es la de algun Goomba
		for(Goomba goomba : goombas){
			if(goomba.isInPosition(position)){
				return String.valueOf(goomba.getIcon());
			}
		}
		//Comprueba si la posicion es la de alguna tierra
		for(Land land : lands){
			if(land.isInPosition(position)){
				return String.valueOf(land.getIcon());
			}
		}
		return " "; //Si no hay nada en esa posicion, devuelve un espacio en blanco
	}

	public void update(){
		
	}
	
}
