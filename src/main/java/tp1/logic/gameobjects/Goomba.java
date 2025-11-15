package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goomba extends MovingObject {

    public Goomba(Game game, Position pos) {
        super(game, pos, -1); //empieza en la izq?
    }

    @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }

    @Override
    public void update() {
        if (!isAlive()) return;

        // 1. Aplicar gravedad
        applyGravity();

        // 2. Movimiento horizontal si no está cayendo
        if (!isFalling) {
            moveHorizontal();
        }
    }
    
    //Metodos que controlan las interacciones:
    @Override
    public boolean interactWith(GameItem other) {
        // Solo tiene sentido interactuar si están en la misma posición
        boolean canInteract = other.isInPosition(this.pos);
        if (!canInteract) {
        	return false;
        }

        return other.receiveInteraction(this);
    }
    
    @Override
    public boolean receiveInteraction(Mario mario) {
    	kill();
    	return true;
    	
    }
    
}