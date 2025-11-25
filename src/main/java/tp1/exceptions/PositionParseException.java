package tp1.exceptions;

public class PositionParseException extends GameModelException{
    public PositionParseException(String message){
        super(message);
    }
    public PositionParseException(String message, Throwable cause){
        super(message, cause);
    }
}
