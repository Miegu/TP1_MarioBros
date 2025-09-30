package tp1.logic;

import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;

public class Game {

	public static final int DIM_X = 30;
	public static final int DIM_Y = 15;

	private int nLevel;
    private int remainingTime;
    private int points;
    private int lives;
    private boolean playerWon;
    private boolean playerLost;

	private GameObjectContainer gameObjects;
    private Mario mario;

	//TODO fill your code
	// Atributos del juego
	
	public Game(int nLevel) {
		// TODO Auto-generated constructor stub
		this.nLevel = nLevel;
		this.remainingTime = 100;
		this.points = 0;
		this.lives = 3;
		this.playerWon = false;
		this.playerLost = false;

		initLevel(nLevel);
	}
	
	private void initLevel(int nLevel) {
		switch(nLevel){
			case 0:
				initLevel0();
				break;
			//case 1:
				//initLevel1();
				//break;
			default:
				initLevel0();
				break;
		}
	}
	
	public String positionToString(int col, int row) {
		Position position = new Position(row, col);
        return gameObjects.positionToString(position);
	}

	public boolean playerWins() {
		// TODO Auto-generated method stub
		return playerWon;
	}

	public boolean playerLoses() {
		// TODO Auto-generated method stub
		return playerLost;
	}  

	public int remainingTime() {
		// TODO Auto-generated method stub
		return remainingTime;
	}

	public int points() {
		// TODO Auto-generated method stub
		return points;
	}

	public int numLives() {
		// TODO Auto-generated method stub
		return lives;
	}

	@Override
	public String toString() {
		// TODO returns a textual representation of the object
		return "Vidas: " + lives + "\nTiempo: " + remainingTime + "\nPuntos: " + points;
	}
	
	
	private void initLevel0() {
		this.nLevel = 0;
		this.remainingTime = 100;
		
		// 1. Mapa
		gameObjects = new GameObjectContainer();
		for(int col = 0; col < 15; col++) {
			gameObjects.add(new Land(new Position(13,col)));
			gameObjects.add(new Land(new Position(14,col)));		
		}

		gameObjects.add(new Land(new Position(Game.DIM_Y-3,9)));
		gameObjects.add(new Land(new Position(Game.DIM_Y-3,12)));
		for(int col = 17; col < Game.DIM_X; col++) {
			gameObjects.add(new Land(new Position(Game.DIM_Y-2, col)));
			gameObjects.add(new Land(new Position(Game.DIM_Y-1, col)));		
		}

		gameObjects.add(new Land(new Position(9,2)));
		gameObjects.add(new Land(new Position(9,5)));
		gameObjects.add(new Land(new Position(9,6)));
		gameObjects.add(new Land(new Position(9,7)));
		gameObjects.add(new Land(new Position(5,6)));
		
		// Salto final
		int tamX = 8, tamY= 8;
		int posIniX = Game.DIM_X-3-tamX, posIniY = Game.DIM_Y-3;
		
		for(int col = 0; col < tamX; col++) {
			for (int fila = 0; fila < col+1; fila++) {
				gameObjects.add(new Land(new Position(posIniY- fila, posIniX+ col)));
			}
		}

		gameObjects.add(new ExitDoor(new Position(Game.DIM_Y-3, Game.DIM_X-1)));

		// 3. Personajes
		this.mario = new Mario(this, new Position(Game.DIM_Y-3, 0));
		gameObjects.add(this.mario);

		gameObjects.add(new Goomba(this, new Position(0, 19)));
	}

}
