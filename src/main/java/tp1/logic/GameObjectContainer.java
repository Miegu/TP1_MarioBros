package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;


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
		return " "; //Si no hay nada en esa posicion, devuelve un espacio en blanco
	}

	public void update(){
		
	}
	
}
