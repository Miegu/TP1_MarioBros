package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
* Comando para salir del juego de forma controlada.
* Responsabilidades:
* - Terminar la partida actual
* - Marcar el juego como finalizado
* - Permitir que el controlador muestre el mensaje de fin
*  Formato: "exit" o "e"
* Diferencia con CTRL+C:
* - Exit: Termina de forma controlada, mostrando mensaje de despedida
* - CTRL+C: Interrumpe abruptamente sin mensaje 
* Este comando es importante para mantener la integridad del programa.
*/
public class ExitCommand extends NoParamsCommand {

    // Forman parte de atributos de estado
    private static final String NAME = Messages.COMMAND_EXIT_NAME;
    private static final String SHORTCUT = Messages.COMMAND_EXIT_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_EXIT_DETAILS;
    private static final String HELP = Messages.COMMAND_EXIT_HELP;

    //Constructor para inicializar el comando de salida
    public ExitCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
    }

    /* Ejecuta el comando para salir del juego.
    * Pasos:
    * 1. Marca el juego como terminado
    * 2. El controlador captura este estado y sale del bucle principal
    * 3. El controlador muestra el mensaje de fin de juego
    * 
    * Nota: El controlador es responsable de mostrar el mensaje de salida, no este comando.  
    * @param game Modelo del juego (marca como terminado)
    * @param view Vista del juego (no utilizado en este comando)
    **/
    @Override
    public void execute(GameModel game, GameView view) {
        // Marcar el juego como terminado
        game.exit();

        //El controller se encarga de mostrar el mensaje de exitgame
    }

    /* Parsea la entrada del usuario para reconocer el comando de salida.
    *Hereda la implementación de NoParamsCommand:
    * - Verifica que la primera palabra coincida con "exit" o "e"
    * - Valida que no haya parámetros adicionales
    * @param commandWords Array de palabras introducidas por el usuario
    * @return ExitCommand si coincide, null si no
    * @throws CommandParseException Si es este comando pero con parámetros extra
    **/
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {
        // Verificar que la primera palabra coincida con el comando
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }

        // Validar que no haya parámetros adicionales
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        
        // Devolver una nueva instancia del comando de salida
        return new ExitCommand();
    }

}
