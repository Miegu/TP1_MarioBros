package tp1.exceptions;

public class OffBoardException extends GameModelException{
	//tb un tipo de excepcion de game model-> para posiciones fiera del tablero
    public OffBoardException(String message){
        super(message);
    }
    public OffBoardException(String message, Throwable cause){
        super(message, cause);
    }
}