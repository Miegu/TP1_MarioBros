package tp1.exceptions;

/**
 * Excepción base para todos los errores relacionados con comandos.
 * 
 * Representa errores en:
 * - Parsing de comandos
 * - Ejecución de comandos
 * - Validación de parámetros de comandos
 * 
 * Jerarquía:
 * - CommandException (padre de todas las excepciones de comandos)
 *   - CommandParseException (parsing incorrecto)
 *   - CommandExecuteException (ejecución fallida)
 * 
 * Diferencia con GameModelException:
 * - CommandException: El comando en sí es inválido
 * - GameModelException: El estado del juego es inválido
 * 
 * Ejemplo de uso:
 * ```java
 * try {
 *     executeCommand(commandTokens);
 * } catch (CommandParseException e) {
 *     System.err.println("Invalid command: " + e.getMessage());
 * } catch (CommandExecuteException e) {
 *     System.err.println("Command execution failed: " + e.getMessage());
 * }
 * ``
 */
public class CommandException extends Exception {
    /**
     * Crea una excepción con un mensaje descriptivo.
     * 
     * @param message Descripción del error del comando
     */
    public CommandException(String message){
        super(message);
    }

    /**
     * Crea una excepción con un mensaje y una causa.
     * 
     * @param message Descripción del error
     * @param cause Excepción original
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
