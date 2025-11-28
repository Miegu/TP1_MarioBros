package tp1.exceptions;

public class GameModelException extends Exception{
	//clase base para tofos los errores de gamemodel
    public GameModelException(String message){
        super(message);
    }

    public GameModelException(String message, Throwable cause){
        super(message, cause);
    }
}