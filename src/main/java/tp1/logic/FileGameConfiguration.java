package tp1.logic;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.ObjectParseException;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameObjectFactory;
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
        Scanner sc = null;

        try {
        	
            //abrimos archivo
            sc = new Scanner(new File(fileName));
            
            //si no hay una linea->archivo vacio lanzamos una exception de tipo gameload
            if (!sc.hasNextLine()) {
                throw new GameLoadException("Empty configuration file: " + fileName);
            }

            String statusLine = sc.nextLine(); //leemos una linea completa del archivo
            String[] parts = statusLine.split(" "); //parte las lineas en trozos con " "

            //si no tenemos 3 elementos ->lanzamos excepcion
            if (parts.length != 3) {
                throw new GameLoadException(""); 
            }

            try {
            	//de los trozitos ponemos a qué corresponde cada uno
                remainingTime = Integer.parseInt(parts[0]);
                points        = Integer.parseInt(parts[1]);
                lives         = Integer.parseInt(parts[2]);
            } 
            catch (NumberFormatException e) {
                throw new GameLoadException("");
            }

            //Leemos el resto de objetos
            while (sc.hasNextLine()) {
            	
            		//leemos la siguiente linea
                String line = sc.nextLine();
                //Separamos la linea en palabras
                String[] objWords = line.split(" ");
                //creamos el objeto 
                GameObject obj = GameObjectFactory.parse(objWords, game);
                
                if (obj.getClass() == Mario.class) { //preguntar si esta bn? se puede hacer asi?
                    mario = (Mario) obj;
                } 
                else {
                    npcObjects.add(obj);
                }
            }

            //comprobamos hemos encontrado a Mario
            if (mario == null) {
                throw new GameLoadException("");
            }

        } 
        
        catch (FileNotFoundException e) {
        	//si archivo no existe->lanzamos excepcin
            throw new GameLoadException("");

        } 
        
        catch (ObjectParseException e) {
            // //error al parsear->lanzamos excepcion
            throw new GameLoadException("");

        }
        
        catch (Exception e) {
            // Cualquier otro error 
            throw new GameLoadException("");

        }
        
        finally {
            // Cerrar scanner
            if (sc != null) {
                sc.close();
            }
        }
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