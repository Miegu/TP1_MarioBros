package tp1.logic;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;

public class Game implements GameModel, GameStatus, GameWorld{

    public static final int DIM_X = 30;
    public static final int DIM_Y = 15;

    private int nLevel;
    private int remainingTime;
    private int points;
    private int lives;
    private boolean playerWon;
    private boolean playerLost;
    private boolean playerExit;

    private GameObjectContainer gameObjects;
    private Mario mario;

    // Atributos del juego
    public Game(int nLevel) {
        this.nLevel = nLevel;
        this.remainingTime = 100;
        this.points = 0;
        this.lives = 3;
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;

        initLevel(nLevel);
    }

    @Override
    public void update() {
        //1 Reducir el tiempo
        if (remainingTime <= 0) {
            playerLost = true;
        } else {
            remainingTime--;
        }
        //2, Actualizar todos los objetos del juego
        gameObjects.update();

        checkInteractions();
    }

    @Override
    public boolean isFinished() {
        return playerWon || playerLost || playerExit;
    }

    @Override
    public void reset() {
        reset(this.nLevel); // Mantiene el nivel actual
    }

    @Override
    public void reset(int level) {
        this.remainingTime = 100;
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;
        this.nLevel = level;
        initLevel(this.nLevel);
    }
    @Override
    public void addAction(Action action) {
        if (mario != null) {
            mario.addAction(action);
        }
    }
    @Override
    public void exit() {
        this.playerExit = true;
    }

    /*GameStatus */
    @Override
    public int points() {
        return points;
    } 

    @Override
    public int numLives() {
        return lives;
    }

    @Override
    public int remainingTime() {
        return remainingTime;
    } 

    @Override
    public String positionToString(int col, int row) {
        Position position = new Position(row, col);
        return gameObjects.positionToString(position);
    }

    /*GameWorld */
    @Override
    public int getRows() {
        return DIM_Y;
    }

    @Override
    public int getCols() {
        return DIM_X;
    }
    @Override
    public boolean isInside(Position pos) {
        if(pos == null) return false;
        return pos.getCol() >= 0 && pos.getCol() < DIM_X
            && pos.getRow() >= 0 && pos.getRow() < DIM_Y;
    }

    @Override
    public boolean isSolid(Position pos) {
       return gameObjects.isSolid(pos);
    }

    @Override
    public boolean addObject(GameObject obj) {
        gameObjects.add(obj);
        return true;
    }

    @Override
    public boolean removeObjectAt(Position pos) {
        return gameObjects.removeObjectAt(pos);
    }  

    @Override
    public GameObject getObjectAt(Position pos) {
        return gameObjects.getObjectAt(pos);
    }

    @Override
    public void addScore(int pointsToAdd){
        this.points += pointsToAdd;
    }

    @Override
    public void loseLife(){
        lives--;
        if(lives <= 0){
            playerLost = true;
        }else{
            //Reiniciar el nivel, pero mantienes puntos y vidas
            int currentPoints = this.points;
            int currentLives = this.lives;
            initLevel(this.nLevel);
            this.points = currentPoints;
            this.lives = currentLives;
        }
    }

    @Override
    public void resetLevel(int levelId){
        reset(levelId);
    }

    @Override
    public void updateAll(){
        update();
    }

    // Metodos adicionales

    /*
	 * Mario llega a la puerta de salida
     */
    public void marioExited() {
        points += remainingTime * 10;
        remainingTime = 0;
        playerWon = true;
    }
    @Override
    public void marioReachedExit() {
        marioExited();
    }

    public void addPoints(int points) {
        addScore(points);
    }

    // Mario muere :c
    public void marioDies() {
        loseLife();
    }

    private void checkInteractions() {
        if (mario == null || !mario.isAlive()) return;
        
        for (GameObject obj : gameObjects.getObjects()) {
            if (obj != mario) {
                mario.interactWith(obj);
            }
        }
    }


    public boolean playerWins() {
        return playerWon;
    }

    public boolean playerLoses() {
        return playerLost;
    }

    @Override
    public String toString() {
        return "Vidas: " + lives + "\nTiempo: " + remainingTime + "\nPuntos: " + points;
    }

    private void initLevel(int nLevel) {
        switch (nLevel) {
            case 0:
                initLevel0();
                break;
            case 1:
                initLevel1();
                break;
            default:
                initLevel0();
                break;
        }
    }

    private void initMap() {

    }

    private void initLevel0() {
        this.nLevel = 0;
        this.remainingTime = 100;

        // 1. Mapa
        gameObjects = new GameObjectContainer();
        for (int col = 0; col < 15; col++) {
            gameObjects.add(new Land(this, new Position(13, col)));
            gameObjects.add(new Land(this, new Position(14, col)));
        }

        gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 9)));
        gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 12)));
        for (int col = 17; col < Game.DIM_X; col++) {
            gameObjects.add(new Land(this, new Position(Game.DIM_Y - 2, col)));
            gameObjects.add(new Land(this, new Position(Game.DIM_Y - 1, col)));
        }

        gameObjects.add(new Land(this, new Position(9, 2)));
        gameObjects.add(new Land(this, new Position(9, 5)));
        gameObjects.add(new Land(this, new Position(9, 6)));
        gameObjects.add(new Land(this, new Position(9, 7)));
        gameObjects.add(new Land(this, new Position(5, 6)));

        // Salto final
        int tamX = 8, tamY = 8;
        int posIniX = Game.DIM_X - 3 - tamX, posIniY = Game.DIM_Y - 3;

        for (int col = 0; col < tamX; col++) {
            for (int fila = 0; fila < col + 1; fila++) {
                gameObjects.add(new Land(this, new Position(posIniY - fila, posIniX + col)));
            }
        }

        gameObjects.add(new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1)));

        // 3. Personajes
        this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));
        gameObjects.add(this.mario);

        gameObjects.add(new Goomba(this, new Position(0, 19)));
    }

    private void initLevel1() {
        initLevel0();
        this.nLevel = 1;

        gameObjects.add(new Goomba(this, new Position(4, 6)));
        gameObjects.add(new Goomba(this, new Position(12, 6)));
        gameObjects.add(new Goomba(this, new Position(12, 8)));
        gameObjects.add(new Goomba(this, new Position(10, 10)));
        gameObjects.add(new Goomba(this, new Position(12, 11)));
        gameObjects.add(new Goomba(this, new Position(12, 14)));
    }

}
