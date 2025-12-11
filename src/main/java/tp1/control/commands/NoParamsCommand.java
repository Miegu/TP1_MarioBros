package tp1.control.commands;

import tp1.exceptions.CommandParseException;
import tp1.view.Messages;
/**
 * Clase abstracta para comandos que no requieren parámetros.
 * 
 * Proporciona una implementación genérica del método parse() que es común
 * a todos los comandos sin parámetros, evitando duplicación de código.
 * 
 * Comandos que heredan de esta clase:
 * - UpdateCommand: Avanza un turno sin acciones
 * - HelpCommand: Muestra ayuda de comandos
 * - ExitCommand: Sale del juego
 * 
 * Ventajas:
 * - DRY: No repetir lógica de parsing de comandos sin parámetros
 * - Mantención: Cambios en la lógica en un solo lugar
 * - Consistencia: Todos los comandos sin parámetros se comportan igual
 */
public abstract class NoParamsCommand extends AbstractCommand {

    /**
     * Constructor para comandos sin parámetros.
     * 
     * @param name Nombre completo del comando
     * @param shortcut Atajo del comando
     * @param details Descripción breve del comando
     * @param help Texto de ayuda detallado del comando
     */
    public NoParamsCommand(String name, String shortcut, String details, String help) {
        super(name, shortcut, details, help);
    }
    
    /**
     * Parsea la entrada del usuario para comandos sin parámetros.
     * 
     * Algoritmo:
     * 1. Valida que haya entrada (array no vacío)
     * 2. Verifica que la primera palabra coincida con el nombre del comando
     * 3. Valida que no haya palabras adicionales (parámetros)
     * 4. Devuelve la instancia del comando si todo es correcto
     * 
     * Casos de error:
     * - Array vacío: No es este comando, devuelve null
     * - Primera palabra no coincide: No es este comando, devuelve null
     * - Más de una palabra: Error de sintaxis, lanza CommandParseException
     * 
     * @param commandWords Array de palabras introducidas por el usuario
     * @return Instancia del comando si coincide y es válido, null si no es este comando
     * @throws CommandParseException Si la sintaxis es incorrecta para este comando
     */
    @Override
    public Command parse(String[] commandWords) throws CommandParseException{
        //Comandos sin parametros: el array debe estar vacio
        if(commandWords.length == 0){
            return null; // No hay entrada, no es este comando
        }

        // Verificar si la primera palabra coincide con el nombre del comando
        if (!matchCommandName(commandWords[0])) {
            return null;  // No es este comando, que otro lo intente
        }
        // Si hay más de 1 palabra Y coincide con el comando, es un error
        // Significa que el usuario escribió el comando con parámetros extra
        if (commandWords.length > 1) {
            throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
        // Si es exactamente 1 palabra y coincide, devolver la instancia del comando
        return this;
    }
    /**
     * Execute() 
     * Método abstracto que debe ser implementado por cada comando específico.
     * Define cómo se ejecuta el comando sobre el modelo y la vista.
     */
}
