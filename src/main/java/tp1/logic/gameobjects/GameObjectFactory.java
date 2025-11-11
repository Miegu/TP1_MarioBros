package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;

public class GameObjectFactory {
    
    public static GameObject parse(String[] words, GameWorld game) {
        //Verifica que hay al menos posicion y tipo de objeto
        if(words == null || words.length < 2){
            return null;
        }
         String objectType = words[0].toLowerCase();
        
        // Formato esperado: "tipo fila columna"
        // Ejemplo: "land 5 10" o "door 3 29"
        
        if (words.length < 3) return null;
        
        try {
            int row = Integer.parseInt(words[1]);
            int col = Integer.parseInt(words[2]);
            Position pos = new Position(row, col);
            
            switch (objectType) {
                case "land", "l":
                    return new Land(game, pos);
                    
                case "door", "d", "exit":
                    return new ExitDoor(game, pos);
                    
                case "goomba", "g":
                    return new Goomba(game, pos);
                    
                case "mario", "m":
                    return new Mario(game, pos);
                    
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}