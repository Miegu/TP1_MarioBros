package tp1.exceptions;

public class CommandParseException extends CommandException {
	//Clase para errores relacionados con lo que escribe el usuario (parsear comando)
    public CommandParseException(String message){
        super(message);
    }
    public CommandParseException(String message, Throwable cause){
        super(message, cause);
    }
}