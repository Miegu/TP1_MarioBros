package tp1.exceptions;

/**
 * Excepción para errores al cargar o guardar el estado del juego desde/a un archivo.
 * 
 * Se lanza cuando:
 * - Archivo no existe
 * - No hay permisos de lectura/escritura
 * - Archivo está corrupto o tiene formato inválido
 * - Error de I/O durante la operación
 * - Disco lleno al guardar
 * - Path inválido
 * 
 * Operaciones:
 * - LOAD: Carga estado del juego desde archivo
 * - SAVE: Guarda estado actual a archivo
 * 
 * Ejemplos de casos:
 * ```java
 * // Archivo no existe
 * if (!fileExists(filename)) {
 *     throw new GameLoadException(
 *         "File not found: " + filename
 *     );
 * }
 * 
 * // Error de I/O
 * try {
 *     BufferedReader reader = new BufferedReader(
 *         new FileReader(filename)
 *     );
 * } catch (IOException e) {
 *     throw new GameLoadException(
 *         "Error reading file: " + filename, e
 *     );
 * }
 * 
 * // Archivo corrupto
 * if (content.isEmpty()) {
 *     throw new GameLoadException(
 *         "Empty or corrupt file: " + filename
 *     );
 * }
 * ```
 * 
 * Jerarquía:
 * - GameLoadException extends Exception (no del modelo)
 */
public class GameLoadException extends Exception {
	public GameLoadException(String message) {
		super(message);
    }
	
	public GameLoadException(String message, Throwable cause) {
		super(message, cause);
    }
}