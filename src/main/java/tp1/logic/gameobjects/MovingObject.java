package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

/**
 * Clase abstracta que representa objetos móviles en el juego.
 * Contiene la lógica común de movimiento y gravedad para Mario y Goomba.
 */
public abstract class MovingObject extends GameObject {

    protected Action direction;
    protected boolean isFalling;

    /**
     * Constructor para objetos móviles
     */
    public MovingObject (GameWorld game, Position pos){
        super(game, pos);
        this.isFalling = false;
        this.direction = Action.RIGHT;
    }
     /**
     * Constructor protegido sin parámetros, para factory
     */
    protected MovingObject() {
        super();
        this.isFalling = false;
        this.direction = Action.RIGHT;
    }
    /**
     * Aplica la gravedad al objeto móvil.
     * El objeto cae si no hay superficie sólida debajo.
     */

     protected void applyGravity(){
        Position currentPos = getPosition();
        Position below = currentPos.down();

        //Si está fuera del tablero, manejar según el tipo de objeto
        if(!game.isInside(currentPos)){
            handleOutOfBounds();
            return;
        }

        //Si la posicion inferior está fuera, el objeto debe caer/morir
        if(!game.isInside(below)){
            setPosition(below);
            isFalling = true;
            handleOutOfBounds();
            return;
        }

        //Si no hay nada sólido debajo, cae
        if(!game.isSolid(below)){
            setPosition(below);
            isFalling = true;
        }else{
            isFalling = false; //Deja de caer cuando toca suelo
        }
     }
     /*
      * Verifica si el objeto está en el suelo( superficie solida debajo)
      * Para centralizar y no duplicar en goomba y Mario
      */
      protected boolean isOnGround(){
        Position currentPos = getPosition();
        //Si está fuera del tablero, no esta en el suelo
        if(!game.isInside(currentPos)){
            return false;
        }

        Position below = currentPos.down();

        //Si ebelow está fuera, pues lo mismo
        if(!game.isInside(below)){
            return false;
        }

        //Solo está en el suelo si below es sólido
        return game.isSolid(below);
      }
    /**
     * Verifica si el objeto puede moverse a una posición dada
     */
    protected boolean canMoveTo(Position position) {
        return game.isInside(position) && !game.isSolid(position);
    }
    /**
     * Método abstracto para manejar cuando el objeto sale del tablero.
     * Cada sobjeto tiene su propio comportamiento (Mario pierde vida, Goomba muere)
     */
    protected abstract void handleOutOfBounds();

    // Getter para saber si está cayendo
    public boolean isFalling() {
        return isFalling;
    }

    // Getter para la dirección
    public Action getDirection() {
        return direction;
    }

    // Setter para la dirección
    protected void setDirection(Action direction) {
        this.direction = direction;
    }
}