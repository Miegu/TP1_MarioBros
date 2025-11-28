package tp1.logic;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.exceptions.GameModelException;
import tp1.logic.gameobjects.Box;
import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.Mushroom;
import tp1.view.Messages;
import java.io.FileWriter;
import java.io.IOException;

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

    // METODOS DE GAME MODEL

    @Override
    public void update() {
        //1 Reducir el tiempo
        if (remainingTime <= 0) {
            playerLost = true;
        } else {
            remainingTime--;
        }
        //2. Actualizar todos los objetos del juego
        gameObjects.update();
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
        //Resetea estado de juego
        this.remainingTime = 100;
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;

        this.nLevel = level;

        initLevel(this.nLevel);

        //Solo resetear vidas si el nivel es -1
        if(level == -1) {
            this.lives = 3;
            this.points = 0;
        }

        
    }

    @Override
    public void addAction(Action action) {
        if (mario != null) {
            mario.addAction(action);
        }
    }
    @Override  
    public void registerAsMain(GameObject obj) {
    // Game conoce a Mario, puede hacer cast
        this.mario = (Mario) obj;
    }

    @Override
    public void exit() {
        this.playerExit = true;
    }

    // METODOS DE GAME STATUS
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
    
    public boolean playerWins() {
        return playerWon;
    }

    public boolean playerLoses() {
        return playerLost;
    }

    // METODOS DE GAME WORLD
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
    public void addObject(GameObject obj) throws OffBoardException{
        //si la pos no esta dentro del tablero lanza la excepcion offboardexception
        if (!isInside(obj.getPosition())) {
            throw new OffBoardException(Messages.ERROR_POSITION_OFF_LIMITS + obj.getPosition());
        }
        
        gameObjects.add(obj);
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
    public void marioReachedExit() {
        points += remainingTime * 10;
        remainingTime = 0;
        playerWon = true;
    }

    @Override
    public void doInteractionsFrom(GameItem item) {
        gameObjects.doInteraction(item);
    }

    // Metodos adicionales    

    private void initLevel(int nLevel) {
        switch (nLevel) {
            case -1:
                initLevelMinus1();
                break;
            case 0:
                initLevel0();
                break;
            case 1:
                initLevel1();
                break;
            case 2:
                initLevel2();
                break;
            default:
                initLevel0();
                break;
        }
    }

    private void initLevelMinus1(){
        this.nLevel = -1;
        this.remainingTime = 100;
        //Mapa vacío para modo creativo
        gameObjects = new GameObjectContainer();
        this.mario = null;
    }

    private void initLevel0() {
        this.nLevel = 0;
        this.remainingTime = 100;
        //Se crea el contenedor de objetos
        gameObjects = new GameObjectContainer();

        //1. Añadir primero a Mario para que sea el primero en actualizarse
        this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));
        gameObjects.add(this.mario);

        // 2. Añadrir el terreno
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
        // 3. Añadir la puerta de salida
        gameObjects.add(new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1)));

        // 3. Añador enemigos al final
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
    
    private void initLevel2() {
        // Igual que nivel 1 pero con extras
        initLevel1();
        
        //Añadir Box en (9,4) - fila 4, columna 9
        gameObjects.add(new Box(this, new Position(9, 4)));
        
        // Añadir Mushrooms en (12,8) y (2,20)
        gameObjects.add(new Mushroom(this, new Position(12, 8)));
        gameObjects.add(new Mushroom(this, new Position(2, 20)));
    }
    
    
    //Metodo para crear archivo de texto con toda la info:
    @Override
    public String toString() {
        //En la primera linea ponemos tiempo, puntos y vidas
        String result = remainingTime + " " + points + " " + lives + "\n";

        //Ponemos una linea por cada objeto
        for (GameObject obj : gameObjects.getObjects()) {
            result = result + obj.serialize() + "\n";
        }

       
        return result;
    }

    
    @Override
    public void save(String fileName) throws GameModelException {
        FileWriter fw = null;

        try {
            //Abrimos el fichero
            fw = new FileWriter(fileName);

            //Escribimos la info del juego
            fw.write(this.toString());

        } catch (IOException e) {
            //Cualquier problema de escritura → GameModelException
            throw new GameModelException("Unable to save game to file: " + fileName, e);

        } finally {
            //Cerrar el fichero si estaba abierto
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignore) {} //ignoramos error
            }
        }
    }

}