package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

import tp1.view.Messages;

public class CommandGenerator {

    private static final List<Command> availableCommands = Arrays.asList(
            new UpdateCommand(),
            new HelpCommand(),
            new ExitCommand(),
            new ResetCommand(),
            new ActionCommand()
    );

    public static Command parse(String[] commandWords) {
        if (commandWords == null || commandWords.length == 0 || (commandWords.length == 1 && commandWords[0].isBlank())) {
            return new UpdateCommand();
        }
        //Probar cada comando disponible		
        for (Command c : availableCommands) {
            Command parsed = c.parse(commandWords);
            if (parsed != null) {
                return parsed;
            }
        }
        return null;
    }

    public static String commandHelp() {
        StringBuilder commands = new StringBuilder();

        commands.append(Messages.HELP_AVAILABLE_COMMANDS).append(Messages.LINE_SEPARATOR);

        for (Command c : availableCommands) {
            commands.append(c.helpText()).append(Messages.LINE_SEPARATOR);
        }

        return commands.toString();
    }

}
