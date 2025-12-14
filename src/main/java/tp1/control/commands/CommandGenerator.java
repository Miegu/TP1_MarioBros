package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

import tp1.exceptions.CommandParseException;
import tp1.view.Messages;

/**
 * Generador y parseador de comandos del juego (Factory Pattern).
 * 
 * Responsabilidades:
 * - Mantener la lista de todos los comandos disponibles
 * - Parsear la entrada del usuario para identificar el comando
 * - Generar instancias del comando correspondiente
 * - Proporcionar ayuda sobre todos los comandos disponibles
 * 
 * Patrón utilizado: Factory Pattern
 * Ventajas:
 * - Centraliza la creación de comandos
 * - Facilita agregar nuevos comandos
 * - Desacopla la creación del uso
 */
public class CommandGenerator {

    /**
     * Lista estática de todos los comandos disponibles en el juego.
     * Se comprueba cada comando en orden hasta encontrar uno que coincida.
     * IMPORTANTE: El orden puede ser relevante si hay comandos similares.
     */
    private static final List<Command> AVAILABLE_COMMANDS = Arrays.<Command>asList(
            new LoadCommand(),
            new SaveCommand(),
            new AddObjectCommand(),
            new ActionCommand(),
            new UpdateCommand(),
            new ResetCommand(),
            new HelpCommand(),
            new ExitCommand()
    );

    /**
     * Parseador principal que identifica y devuelve el comando correspondiente.
     * 
     * Algoritmo:
     * 1. Valida que la entrada no sea nula ni vacía
     * 2. Intenta parsear con cada comando disponible en orden
     * 3. Devuelve el primero que reconozca la entrada
     * 4. Lanza excepción si ninguno reconoce la entrada
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Comando parseado y listo para ejecutar
     * @throws CommandParseException Si la entrada no corresponde a ningún comando conocido
     */
    public static Command parse(String[] commandWords) throws CommandParseException {
        // Si no hay entrada o es nula, devolver excepcion
        if (commandWords == null || commandWords.length == 0) {
            throw new CommandParseException(Messages.UNKNOWN_COMMAND.formatted(""));
        }

        //Probar cada comando disponible, hasta encontrar uno que coincida	
        for (Command c : AVAILABLE_COMMANDS) {
            Command parsed = c.parse(commandWords);
            if (parsed != null) {
                return parsed;
            }
        }
        // Si ningún comando reconoce la entrada, lanzar excepción
        throw new CommandParseException(Messages.UNKNOWN_COMMAND.formatted(commandWords[0])
        );
    }

    /**
     * Genera el texto de ayuda completo de todos los comandos disponibles.
     * 
     * Formato de salida:
     * ```
     * Available commands:
     * [SHORTCUT, NAME]: DETAILS
     * [SHORTCUT, NAME]: DETAILS
     * ...
     * ```
     * 
     * @return String con la ayuda de todos los comandos disponibles
     */
    public static String commandHelp() {
        StringBuilder commands = new StringBuilder();

        commands.append(Messages.HELP_AVAILABLE_COMMANDS).append(Messages.LINE_SEPARATOR);

        for (Command c : AVAILABLE_COMMANDS) {
            commands.append(c.helpText()).append(Messages.LINE_SEPARATOR);
        }

        return commands.toString();
    }

}
