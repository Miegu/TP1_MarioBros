package tp1.logic;

import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;

public class Game implements GameModel, GameStatus, GameWorld {

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
    private boolean playerExited;

    public Game(int nLevel) {
        this.nLevel = nLevel;
        this.remainingTime = 100;
        this.points = 0;
        this.lives = 3;
        this.playerWon = false;
        this.playerLost = false;
        this.playerExited = false;
        initLevel(nLevel);
    }

    public void update() {
        //restar tiempo
        if (remainingTime <= 0) {
            playerLost = true;
        } else {
            remainingTime--;
        }
        //actualizar todos los objetos
        gameObjects.update();
    }

    public void addAction(Action accion) {
        if (mario != null) {
            mario.addAction(accion);
        }
    }
    
    //mario consigue salie
    public void marioExited() {
        points += remainingTime * 10;
        playerWon = true;
    }
    
    
    //método para sumar puntos
    public void addPoints(int points) {
        this.points += points;
    }

    //metodo para cuando mario muere
    public void marioDies() {
        lives--;
        if (lives <= 0) {
            playerLost = true;
        } else {
            int currentPoints = this.points;
            int currentLives = this.lives;
            initLevel(this.nLevel);
            this.points = currentPoints;
            this.lives = currentLives;
        }
    }

    public GameObjectContainer getGameObjects() {
        return gameObjects;
    }

    //metodo para devolver el icono segun el objeto en una det posiciom
    public String positionToString(int col, int row) {
        Position position = new Position(row, col);
        return gameObjects.getIconAt(position); 
    }

    //getters y setters de los atributos:
    public boolean playerWins() {
        return playerWon;
    }

    public boolean playerLoses() {
        return lives <= 0;
    }

    public int remainingTime() {
        return remainingTime;
    }

    public int points() {
        return points;
    }

    public int numLives() {
        return lives;
    }

    @Override
    public String toString() {
        return "Vidas: " + lives + "\nTiempo: " + remainingTime + "\nPuntos: " + points;
    }
    
    //metodo para iniciar el nivel indicado
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

    //nivel 0
    private void initLevel0() {
        this.nLevel = 0;
        this.remainingTime = 100;

        gameObjects = new GameObjectContainer();

        // 1. Suelo base
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

        int tamX = 8, tamY = 8;
        int posIniX = Game.DIM_X - 3 - tamX, posIniY = Game.DIM_Y - 3;

        for (int col = 0; col < tamX; col++) {
            for (int fila = 0; fila < col + 1; fila++) {
                gameObjects.add(new Land(this, new Position(posIniY - fila, posIniX + col)));
            }
        }

        gameObjects.add(new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1)));
        this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));
        gameObjects.add(this.mario);
        gameObjects.add(new Goomba(this, new Position(0, 19)));
    }

    
    //nivel 1
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

    public void reset() {
        reset(this.nLevel);
    }

    public void reset(int level) {
        this.remainingTime = 100;

        if (level == 0 || level == 1) {
            this.nLevel = level;
        }

        initLevel(this.nLevel);
    }

    public void exit() {
        this.playerExited = true;
    }

    public boolean isFinished() {
        return this.remainingTime == 0 || this.playerLost || this.playerWon || this.playerExited;
    }

    public void doInteractionsFrom(GameItem item) {
        gameObjects.doInteraction(item);
    }
}
