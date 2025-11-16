package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

import tp1.view.Messages;
/**
 * Generador de comandos del juego.
 * Parsea la entrada del usuario y devuelve el comando correspondiente.
 */
public class CommandGenerator {

    private static final List<Command> AVAILABLE_COMMANDS = Arrays.<Command>asList(
            new UpdateCommand(),
            new HelpCommand(),
            new ExitCommand(),
            new ResetCommand(),
            new ActionCommand(),
            new AddObjectCommand()
    );

    /**
     * Parsea la entrada del usuario y devuelve el comando correspondiente.
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Comando parseado o null si no se reconoce
     */
    public static Command parse(String[] commandWords) {
        // Si no hay entrada, devolver null
        if (commandWords == null || commandWords.length == 0) {
            return null;
        }
        //Probar cada comando disponible		
        for (Command c : AVAILABLE_COMMANDS) {
            Command parsed = c.parse(commandWords);
            if (parsed != null) {
                return parsed;
            }
        }
        // Si ningún comando reconoce la entrada, devolver null
        return null;
    }

    /**
     * Devuelve el texto de ayuda de todos los comandos disponibles.
     * 
     * @return String con la ayuda de todos los comandos
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
