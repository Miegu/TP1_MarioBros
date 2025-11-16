package tp1.control.commands;

import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Clase abstracta que encapsula la funcionalidad común de todos los comandos.
 * Almacena el nombre, atajo, detalles y texto de ayuda del comando.
 */
public abstract class AbstractCommand implements Command {

	// Forman parte de atributos de estado
	private final String name;
	private final String shortcut;
	private final String details;
	private final String help;
	
	/**
     * Constructor para inicializar un comando con su información.
     * 
     * @param name Nombre completo del comando
     * @param shortcut Atajo del comando (versión corta)
     * @param details Descripción breve del comando
     * @param help Texto de ayuda completo
     */
	public AbstractCommand(String name, String shortcut, String details, String help) {
		this.name = name;
		this.shortcut = shortcut;
		this.details = details;
		this.help = help;
	}

	/**
     * Verifica si la entrada del usuario coincide con este comando.
     * Compara con el nombre completo o el atajo.
     * 
     * @param commandName Nombre introducido por el usuario
     * @return true si coincide con el nombre o atajo, false en caso contrario
     */
    protected boolean matchCommandName(String commandName) {
        return commandName.equalsIgnoreCase(name) || 
               commandName.equalsIgnoreCase(shortcut);
    }

	
	protected String getName() { return name; }
	protected String getShortcut() { return shortcut; }
	protected String getDetails() { return details; }
	protected String getHelp() { return help; }

	/**
     * Devuelve el texto de ayuda formateado para este comando.
     * Formato: "[SHORTCUT, NAME]: DETAILS"
     * 
     * @return String con la ayuda del comando
     */
	@Override
	public String helpText(){
		return Messages.LINE_TAB.formatted(Messages.COMMAND_HELP_TEXT.formatted(getDetails(), getHelp()));
	}
	
	// execute() y parse() se implementan en las subclases
	@Override
	public abstract void execute(GameModel game, GameView view);

	@Override
	public abstract Command parse(String[] words);
}
