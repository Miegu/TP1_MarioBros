package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;

/**
 * Interfaz que define el contrato para todos los comandos del juego.
 *
 * Implementa el patrón Command, permitiendo encapsular solicitudes como
 * objetos. Todos los comandos del juego deben implementar esta interfaz.
 *
 * Los comandos disponibles son: 
 * - ActionCommand: Procesa acciones de Mario (movimiento, salto, etc.) 
 * - AddObjectCommand: Añade objetos al tablero 
 * - UpdateCommand: Actualiza un ciclo del juego 
 * - ResetCommand: Reinicia el juego
 * - SaveCommand: Guarda el estado actual del juego 
 * - LoadCommand: Carga un juego previamente guardado 
 * - HelpCommand: Muestra ayuda sobre los comandos disponibles 
 * - ExitCommand: Sale del juego de forma controlada
 *
 * Nota: En las interfaces todos los métodos son public y abstract por defecto.
 */
public interface Command {

    /**
     * Ejecuta el comando sobre el modelo del juego y actualiza la vista.
     *
     * @param game Modelo del juego sobre el que se ejecutará el comando
     * @param view Vista del juego que se actualizará con el resultado
     * @throws CommandExecuteException Si ocurre un error durante la ejecución
     * del comando
     */
    void execute(GameModel game, GameView view) throws CommandExecuteException;

    /**
     * Intenta parsear la entrada del usuario para crear una instancia de este
     * comando.
     *
     * Método de factory que comprueba si la entrada del usuario corresponde a
     * este comando. 
     * Si coincide, devuelve una instancia del comando inicializada con los parámetros parseados. 
     * Si no coincide, devuelve null para que otros comandos lo intenten.
     *
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Una instancia del comando si coincide con la entrada, null si no
     * es este comando
     * @throws CommandParseException Si la entrada es para este comando pero
     * tiene formato inválido
     */
    Command parse(String[] commandWords) throws CommandParseException;

    /**
     * Devuelve el texto de ayuda para este comando.
     *
     * Proporciona una descripción de cómo usar el comando, incluyendo su
     * sintaxis y parámetros esperados.
     *
     * @return String con la ayuda formateada del comando (formato: "[SHORTCUT,
     * NAME]: DETAILS")
     */
    String helpText();
}
