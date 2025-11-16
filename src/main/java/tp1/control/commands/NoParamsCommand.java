package tp1.control.commands;
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
    public Command parse(String[] commandWords) {
        //Comandos sin parametros: solo debe haber una palabra
        if (commandWords != null && commandWords.length == 1) {
            // Usa el metodo heredado para verificar si coincide
            if (matchCommandName(commandWords[0])) {
                return this; // devuelve el mismo comando
            }
        }
        return null; //No coincide con este comando
    }
    // execute() debe ser implementado por cada comando en concreto
}
