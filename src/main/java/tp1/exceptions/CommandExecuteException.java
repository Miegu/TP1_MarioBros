package tp1.exceptions;

public class CommandExecuteException extends CommandException {
	//Clase que representa errores al ejecutar un comando
	
    public CommandExecuteException(String message){
        super(message);
    }
    public CommandExecuteException (String message, Throwable cause){
        super(message, cause);
    }
}