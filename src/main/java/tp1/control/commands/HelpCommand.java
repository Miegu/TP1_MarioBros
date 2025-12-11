package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/** Comando para mostrar la ayuda del juego.
 *  Responsabilidades:
 * - Obtener la lista completa de comandos disponibles
 * - Mostrar la sintaxis y descripción de cada comando
 * - Facilitar que el usuario entienda cómo usar el juego
 * Formato: "help" o "h"
 * Muestra:
 * - Lista de todos los comandos disponibles
 * - Nombre completo y atajo de cada comando
 * - Descripción breve de cada comando
 * - Texto de ayuda detallado
 *
 * Este comando es crítico para la usabilidad del juego.
 * 
*/
public class HelpCommand extends NoParamsCommand {

    private static final String NAME = Messages.COMMAND_HELP_NAME;
    private static final String SHORTCUT = Messages.COMMAND_HELP_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_HELP_DETAILS;
    private static final String HELP = Messages.COMMAND_HELP_HELP;
    
    //Constructor para inicializar el comando de ayuda.
    public HelpCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    /* Ejecuta el comando para mostrar ayuda.
    * Pasos:     
    * 1. Obtiene la ayuda de todos los comandos disponibles
    * 2. Muestra la ayuda completa en la vista
    * 3. No cambia el estado del juego
    * 4. No actualiza el tablero
    * @param game Modelo del juego (no se modifica)    
    * @param view Vista del juego (solo para mostrar mensajes)
    */
    @Override
    public void execute(GameModel game, GameView view) {
        // Obtener la ayuda de todos los comandos disponibles
        String helpText = CommandGenerator.commandHelp();
        // Mostrar la ayuda en la vista
        view.showMessage(helpText);
    }

    /* Parsea la entrada del usuario para reconocer el comando de ayuda
    * Hereda la implementación de NoParamsCommand:
    *   - Verifica que la primera palabra coincida con "help" o "h" 
    *   - Valida que no haya parámetros adicionales 
    * @param commandWords Array de palabras introducidas por el usuario
    * @return HelpCommand si coincide, null si no
    * @throws CommandParseException Si es este comando pero con parámetros extra   
    **/
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        //Verificar que la primera palabra coincida con el comando
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }

        // Validar que no haya parámetros adicionales
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }

        // Devolver una nueva instancia del comando de ayuda
        return new HelpCommand();
    }

}
