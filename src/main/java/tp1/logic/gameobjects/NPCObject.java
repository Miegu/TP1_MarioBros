package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
public abstract class NPCObject extends MovingObject{

    public NPCObject(GameWorld game, Position pos){
        super(game, pos);
        direction = Action.LEFT;
    }

    protected NPCObject(){
        super();
        direction = Action.LEFT;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        
        // 1. Gravedad
        applyGravity();
        game.doInteractionsFrom(this);
        
        // 2. Movimiento (si no está cayendo)
        if (!isFalling && isAlive()) {
            performNPCBehavior();
        }
    }

    /**
     * Realiza el movimiento horizontal estandar de NPC.
     * Si choca con un obstáculo, cambia de dirección.
     */
    protected void performHorizontalMovement() {
        // Intentar moverse en la dirección actual
        if (!move(direction)) {
            // Si el movimiento falló (chocó con algo), cambiar dirección
            direction = (direction == Action.LEFT) ? Action.RIGHT : Action.LEFT;
        } else {
            // Movimiento exitoso, verificar interacciones
            if(isAlive()) game.doInteractionsFrom(this);
        }
    }

    /**
     * Método que pueden sobrescribir NPCs con comportamiento especial
     */
    protected void performNPCBehavior() {
        performHorizontalMovement();  // Por defecto, patrulla simple
    }
    
    @Override
    public boolean canBeRemoved() {
        return true;
    }
    
    @Override
    protected void handleOutOfBounds() {
        dead();
    }
    
    @Override
    public boolean isSolid() {
        return false;
    }

}
