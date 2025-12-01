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
import tp1.view.Messages;

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
            // Abrimos archivo
            sc = new Scanner(new File(fileName));
            
            // Si no hay una línea -> archivo vacío
            if (!sc.hasNextLine()) {
                throw new GameLoadException(
                    Messages.ERROR_EMPTY_FILE + fileName 
                );
            }

            String statusLine = sc.nextLine();
            String[] parts = statusLine.split(" ");

            // Si no tenemos 3 elementos
            if (parts.length != 3) {
                throw new GameLoadException(
                    Messages.ERROR_INVALID_GAME_STATUS 
                );
            }

            try {
                remainingTime = Integer.parseInt(parts[0]);
                points        = Integer.parseInt(parts[1]);
                lives         = Integer.parseInt(parts[2]);
            } 
            catch (NumberFormatException e) {
                throw new GameLoadException(
                    Messages.ERROR_INVALID_GAME_STATUS,  
                    e
                );
            }

            // Leemos el resto de objetos
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                
                // Saltar líneas vacías
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] objWords = line.split(" ");
                GameObject obj = GameObjectFactory.parse(objWords, game);
                
                // Si el tipo del objeto es "Mario" o "M", guardarlo como mario
                if (objWords.length >= 2) {
                    String objectType = objWords[1].toUpperCase();
                    
                    if (objectType.equals("MARIO") || objectType.equals("M")) {
                        mario = (Mario) obj;
                    } else {
                        npcObjects.add(obj);
                    }
                }
            }

            // Comprobamos hemos encontrado a Mario
            if (mario == null) {
                throw new GameLoadException(
                    Messages.ERROR_NO_MARIO
                );
            }

        } 
        
        catch (FileNotFoundException e) {
            throw new GameLoadException(
                Messages.ERROR_FILE_NOT_FOUND_QUOTED.formatted(fileName), 
                e
            );
        } 
        
        catch (ObjectParseException e) {
            throw new GameLoadException(
                Messages.ERROR_INVALID_GAME_OBJECT_FILE,  
                e
            );
        }
        
        catch (GameLoadException e) {
            // Re-lanzar GameLoadException sin envolver
            throw e;
        }
        
        catch (Exception e) {
            throw new GameLoadException(
                Messages.ERROR_LOADING_GAME + fileName,  
                e
            );
        }
        
        finally {
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
