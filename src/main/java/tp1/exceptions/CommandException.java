package tp1.exceptions;

public class CommandException extends Exception {
	//Exception es la clase java para excepciones que sirve para cualquier error relacionado con comandos 
    public CommandException(String message){
        super(message);
    }
    //Constructor q recibe un mensaje de error y se lo pasa al constructr de exception
    //ese mensaje se imprimira con getmessage
    
    
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
    //Constructor que recibe mensaje y causa (causa->otra excepcion que provocó la excepcion)
    //guardamois tanto la causa como el mensaje
    
}