package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;
/**
 * Comando para reiniciar el juego.
 * 
 * Responsabilidades:
 * - Parsear el nivel a reiniciar (opcional)
 * - Validar que el nivel sea válido
 * - Reiniciar el juego al nivel especificado o al actual
 * - Actualizar la vista
 * 
 * Formatos:
 * - "reset" o "r": Reinicia el nivel actual
 * - "reset <nivel>" o "r <nivel>": Reinicia un nivel específico
 * 
 * Niveles válidos: -1 (menú), 0 (nivel 1), 1 (nivel 2), 2 (nivel 3)
 * 
 * Este comando es útil para empezar de nuevo sin salir del juego.
 */
public class ResetCommand extends AbstractCommand {

    private static final String NAME = Messages.COMMAND_RESET_NAME;
    private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_RESET_DETAILS;
    private static final String HELP = Messages.COMMAND_RESET_HELP;

     /** Nivel a resetear. Null significa el nivel actual. */
    private Integer level;

    /**
     * Constructor por defecto sin especificar nivel.
     * Reinicia el nivel actual cuando se ejecuta.
     */
    public ResetCommand() {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = null; // Valor por defecto, resetea al nivel actual
    }
    /**
     * Constructor con nivel específico.
     * Reinicia el nivel especificado cuando se ejecuta.
     * 
     * @param level Número del nivel a resetear (-1, 0, 1, o 2)
     */
    public ResetCommand(int level) {
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = level;
    }

    /**
     * Parsea la entrada del usuario para extraer el nivel (si aplica).
     * 
     * Algoritmo:
     * 1. Verifica que la primera palabra coincida con el comando
     * 2. Si solo hay una palabra, devuelve comando sin nivel (actual)
     * 3. Si hay dos palabras, intenta parsear la segunda como número
     * 4. Si hay más de dos palabras, lanza error de sintaxis
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return ResetCommand con o sin nivel especificado
     * @throws CommandParseException Si la sintaxis es incorrecta
     */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException {

        // IMPORTANTE: Verificar que la primera palabra sea "reset" o "r"
        if (!matchCommandName(commandWords[0])) {
            return null; // No es este comando, que otro lo intente
        }
        // Si solo es "reset", devolver instancia sin nivel (actual)
        if (commandWords.length == 1) {
            return new ResetCommand();
        }

        // Si tiene 2 palabras, parsear el nivel
        if (commandWords.length == 2) {
            try {
                // Intentar convertir la segunda palabra a número
                int level = Integer.parseInt(commandWords[1]);
                return new ResetCommand(level);
            } catch (NumberFormatException nfe) {
                // Envolver la excepción
                // La segunda palabra no es un número válido
                throw new CommandParseException(
                        Messages.LEVEL_NOT_A_NUMBER_ERROR.formatted(commandWords[1]),
                        nfe
                );
            }
        }
        // Si tiene más de 2 parámetros, error de sintaxis
        throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
    }

    /**
     * Ejecuta el comando para reiniciar el juego.
     * 
     * Pasos:
     * 1. Si hay un nivel especificado:
     *    a. Valida que el nivel esté en el rango válido (-1 a 2)
     *    b. Reinicia ese nivel específico
     * 2. Si no hay nivel especificado:
     *    a. Reinicia el nivel actual
     * 3. Actualiza la vista con el nuevo estado
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si el nivel especificado no es válido
     */
    @Override
    public void execute(GameModel game, GameView view) throws CommandExecuteException {
        // Si se especificó un nivel, validar y resetear a ese nivel
        if (level != null) {
            // Validar que el nivel esté en el rango válido (-1 a 2)
            if (level < -1 || level > 2) {
                throw new CommandExecuteException("Not valid level number");
            }
            // Resetear el juego al nivel especificado
            game.reset(level);
        } else {
            // Resetear el juego al nivel actual
            game.reset();
        }

        // Mostrar el nuevo estado del juego en la vista
        view.showGame();
    }
}
