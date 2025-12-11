package tp1.exceptions;

/**
 * Excepción para errores al parsear comandos desde la entrada del usuario.
 * 
 * Se lanza cuando:
 * - Comando desconocido (ej: "xyzzy" en lugar de "help")
 * - Número incorrecto de parámetros
 * - Parámetros con formato inválido
 * - Alias de comando inválido
 * 
 * Comandos válidos:
 * - update (u) - Sin parámetros
 * - action (a) - Direcciones: LEFT, RIGHT, UP, DOWN, STOP
 * - help (h) - Sin parámetros
 * - exit (e) - Sin parámetros
 * - reset (r) - Parámetro opcional: número de nivel
 * - save (s) - Parámetro obligatorio: nombre de archivo
 * - load (l) - Parámetro obligatorio: nombre de archivo
 * 
 * Ejemplos de casos:
 * ```java
 * // Comando desconocido
 * if (!isValidCommand(commandName)) {
 *     throw new CommandParseException(
 *         "Unknown command: " + commandName
 *     );
 * }
 * 
 * // Parámetros incorrectos
 * if (commandName.equals("action") && params.isEmpty()) {
 *     throw new CommandParseException(
 *         "Action command requires direction parameters"
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - CommandParseException extends CommandException
 */
public class CommandParseException extends CommandException {
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error de parsing
     *        (ej: "Unknown command: xyzzy")
     */
    public CommandParseException(String message){
        super(message);
    }
    
    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error de parsing
     * @param cause Excepción original que causó este error
     */
    public CommandParseException(String message, Throwable cause){
        super(message, cause);
    }
}
