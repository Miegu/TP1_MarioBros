package tp1.exceptions;

public class ObjectParseException extends GameModelException{
    public ObjectParseException(String message){
        super(message);
    }
    public ObjectParseException(String message, Throwable cause){
        super(message, cause);
    }
}