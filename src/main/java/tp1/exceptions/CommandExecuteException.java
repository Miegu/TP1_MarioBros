package tp1.exceptions;

/**
 * Excepción para errores durante la ejecución de un comando.
 * 
 * Se lanza cuando:
 * - Comando sintácticamente válido pero no se puede ejecutar
 * - Archivo no existe al hacer LOAD
 * - No hay permisos de escritura para SAVE
 * 
 * Diferencia con CommandParseException:
 * - Parse: "Unknown command: xyzzy" ← Comando no existe
 * 
 * Ejemplos de casos:
 * 
 * // Error de I/O
 * try {
 *     saveToFile(filename);
 * } catch (IOException e) {
 *     throw new CommandExecuteException(
 *         "Cannot save to file: " + filename, e
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - CommandExecuteException extends CommandException
 */
public class CommandExecuteException extends CommandException {
    public CommandExecuteException(String message){
        super(message);
    }
    public CommandExecuteException (String message, Throwable cause){
        super(message, cause);
    }
}
