package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.GameItem;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mushroom extends MovingObject {

    // Constructor
    public Mushroom(GameWorld game, Position pos) {
        // dir = 1 pq empieza moviéndose a la derecha
        super(game, pos, 1);
    }

    @Override
    public String getIcon() {
        return Messages.MUSHROOM;
    }

    @Override
    public void update() {
        if (!isAlive()) return;
        applyGravity();
        if (!isFalling) {
            moveHorizontal();
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    
    //Metodos que controlan las interacciones
    @Override
    public boolean interactWith(GameItem other) {
        //igual que Goomba
        boolean canInteract = other.isInPosition(this.pos);
        if (!canInteract) {
        	return false;
        }
        return other.receiveInteraction(this);
    }

    
    // Interacción con mario
    @Override
    public boolean receiveInteraction(Mario mario) {
        if (!isAlive()) {
        	return false;
        }

        if (mario.isBig()) {
            this.kill();
        } 
        else {
            //si mario pequeño-> se hace grande
            mario.setBig(true);
            this.kill();
        }
        return true;
    }

   
}
