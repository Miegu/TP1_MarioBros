package tp1.exceptions;

public class ActionParseException extends GameParseException{
    public ActionParseException(String message){
        super(message);
    }
    public ActionParseException(String message, Throwable cause){
        super(message, cause);
    }
}
