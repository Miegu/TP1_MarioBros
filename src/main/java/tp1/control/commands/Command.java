package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;

/* Interfaz que representa un comando del juego.
 * Todos los comandos deben implementar esta interfaz.
 * En las interfaces todos los metodos son public y abstract por defecto
*/
public interface Command {
    /**
     * Ejecuta el comando sobre el modelo del juego y actualiza la vista.
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     */
    void execute(GameModel game, GameView view) throws CommandExecuteException;
   /**
     * Intenta parsear la entrada del usuario para crear una instancia del comando.
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Una instancia del comando si coincide con la entrada, null en caso contrario
     */
    Command parse(String[] commandWords) throws CommandParseException;

    /**
     * Devuelve el texto de ayuda para este comando.
     * 
     * @return String con la ayuda del comando (formato: "COMMAND: help text")
     */
    String helpText();
}
