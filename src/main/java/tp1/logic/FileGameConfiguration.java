package tp1.logic;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import tp1.exceptions.ObjectParseException;
import tp1.exceptions.OffBoardException;
import tp1.exceptions.GameLoadException;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Mario;

//clase encargada de representar la configuracion de juego cargada de un archivo
public class FileGameConfiguration implements GameConfiguration {

    //Estado del juego
    private int remainingTime;
    private int points;
    private int lives;

    private Mario mario; //para guardar la referencia al mario cargado del archivo
    private List<GameObject> npcObjects; //El resto de objetos 

   
    public FileGameConfiguration(String fileName, GameWorld game) throws GameLoadException {
		npcObjects = new ArrayList<>(); //lista donde vamos a ir metiendo los objetos (excepto mario) que leamos del archivo
	    loadFromFile(fileName, game); //método que lee el fichero y rellena los atributos
        	
    }

    private void loadFromFile(String fileName, GameWorld game) throws GameLoadException {
    	
    }

    @Override
    public int getRemainingTime() {
    	return this.remainingTime; 
    }

    @Override
    public int getPoints() { 
    	return this.points; 
    }

    @Override
    public int getNumLives() {
    	return this.lives; 
    }

    @Override
    public Mario getMario() {
    	return this.mario; 
    }

    @Override
    public List<GameObject> getNPCObjects() { 
    	return npcObjects; 
    }
}
