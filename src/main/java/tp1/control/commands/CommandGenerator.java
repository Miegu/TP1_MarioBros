package tp1.control.commands;

import java.util.Arrays;
import java.util.List;
import tp1.exceptions.CommandParseException;
import tp1.view.Messages;
/**
 * Generador de comandos del juego.
 * Parsea la entrada del usuario y devuelve el comando correspondiente.
 */
public class CommandGenerator {

    private static final List<Command> AVAILABLE_COMMANDS = Arrays.<Command>asList(
        new AddObjectCommand(),
        new ActionCommand(),
        new UpdateCommand(),
        new ResetCommand(),
        new HelpCommand(),
        new ExitCommand(),
        new SaveCommand()
    );

    
    public static Command parse(String[] words) throws CommandParseException {
        for (Command c : AVAILABLE_COMMANDS) {
            Command parsed = c.parse(words);
            if (parsed != null) {
                return parsed;
            }
        }
        	throw new CommandParseException(
            Messages.UNKNOWN_COMMAND.formatted(words[0])
        );
    }


    public static String commandHelp() {
        StringBuilder commands = new StringBuilder();

        commands.append(Messages.HELP_AVAILABLE_COMMANDS).append(Messages.LINE_SEPARATOR);

        for (Command c : AVAILABLE_COMMANDS) {
            commands.append(c.helpText()).append(Messages.LINE_SEPARATOR);
        }

        return commands.toString();
    }

}