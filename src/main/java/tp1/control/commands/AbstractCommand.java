package tp1.control.commands;

import tp1.exceptions.CommandExecuteException;
import tp1.exceptions.CommandParseException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Clase abstracta que encapsula la funcionalidad común de todos los comandos.
 *
 * Proporciona: 
 * - Almacenamiento de metadatos del comando (nombre, atajo, detalles, ayuda) 
 * - Método para verificar si una entrada coincide con este comando 
 * - Implementación del método helpText() para formateo estándar 
 * - Define métodos abstractos que deben implementar las subclases
 *
 * Esta clase reduce la duplicación de código entre diferentes comandos,
 * siguiendo el principio DRY (Don't Repeat Yourself).
 */
public abstract class AbstractCommand implements Command {

    /** Nombre completo del comando (ej: "addObject", "reset"). */
    private final String name;
	/** Atajo o versión corta del comando (ej: "aO", "r"). */
    private final String shortcut;
	/** Descripción breve del comando para el usuario. */
    private final String details;
	/** Texto de ayuda completo del comando. */
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
     * 
     * Compara la entrada con el nombre completo o el atajo del comando.
     * La comparación es case-insensitive (no distingue mayúsculas/minúsculas).
     * 
     * @param commandName Nombre introducido por el usuario
     * @return true si coincide con el nombre completo o atajo, false en caso contrario
     */
    protected boolean matchCommandName(String commandName) {
        return commandName.equalsIgnoreCase(name)
                || commandName.equalsIgnoreCase(shortcut);
    }

	/**
     * Obtiene el nombre completo del comando.
     * @return Nombre del comando
     */
    protected String getName() {
        return name;
    }

    protected String getShortcut() {
        return shortcut;
    }

    protected String getDetails() {
        return details;
    }

    protected String getHelp() {
        return help;
    }

    /**
     * Devuelve el texto de ayuda formateado para este comando.
     * 
     * Formatea la ayuda del comando siguiendo un patrón consistente en toda la aplicación.
     * Formato: "[SHORTCUT, NAME]: DETAILS"
     * 
     * @return String con la ayuda del comando formateada correctamente
     */
    @Override
    public String helpText() {
        return Messages.COMMAND_HELP_TEXT.formatted(getDetails(), getHelp());
    }

    /**
     * Ejecuta el comando. Debe ser implementado por cada subclase específica.
     * 
     * @param game Modelo del juego
     * @param view Vista del juego
     * @throws CommandExecuteException Si ocurre un error durante la ejecución
     */
    @Override
    public abstract void execute(GameModel game, GameView view) throws CommandExecuteException;
	
	/**
     * Parsea la entrada del usuario. Debe ser implementado por cada subclase específica.
     * 
     * @param words Array de palabras del usuario
     * @return Instancia del comando si coincide, null si no
     * @throws CommandParseException Si la sintaxis es incorrecta
     */
    @Override
    public abstract Command parse(String[] words) throws CommandParseException;
}
