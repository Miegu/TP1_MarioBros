package tp1.control.commands;
import tp1.exceptions.CommandParseException;
import tp1.view.Messages;
/**
 * Clase abstracta para comandos que no tienen parámetros.
 * Implementa el método parse() de forma genérica para todos los comandos sin parámetros.
 */
public abstract class NoParamsCommand extends AbstractCommand {
     /**
     * Constructor para comandos sin parámetros.
     * */
    public NoParamsCommand(String name, String shortcut, String details, String help) {
        super(name, shortcut, details, help);
    }
    /**
     * Parsea la entrada del usuario para comandos sin parámetros.
     * Solo verifica que el usuario haya introducido una única palabra
     * que coincida con el nombre o atajo del comando.
     * */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException{
        //Comandos sin parametros: solo debe haber una palabra
        if(commandWords.length == 0){
            return null;
        }
        // Primero verifica si coincide con el nombre
        if (!matchCommandName(commandWords[0])) {
            return null;  // No es este comando, que otro lo intente
        }
        // Si hay más de 1 palabra Y coincide con el comando, es un error
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        // Si es exactamente 1 palabra y coincide, devolver el comando
        if (commandWords.length == 1 && matchCommandName(commandWords[0])) {
            return this;
        }
        
        return null; //No coincide con este comando
    }
    // execute() debe ser implementado por cada comando en concreto
}
