package tp1.exceptions;

public class ObjectParseException extends GameModelException{
	//es un tipo de error de modelo
	//clase para excepciones relacionadas con parsear un objeto del juego
	//ej cuando ponemos (3,2)goomba right
    public ObjectParseException(String message){
        super(message);
    }
    public ObjectParseException(String message, Throwable cause){
        super(message, cause);
    }
}