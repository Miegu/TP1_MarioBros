package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goomba extends GameObject {

    private int direction; // -1 izquierda, 1 derecha Wombat empieza hacia la izq
    private boolean isFalling;

    protected Goomba(){
        super();
    }
    
    public Goomba(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = -1; //Empieza mirando a la izquierda
    }

    @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }
    public boolean isFalling() {
        return isFalling;
    }

     @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        
        // 1: Apply gravity
        applyGravity();
        
        // 2: Horizontal movement if not falling
        if (!isFalling) {
            Position currentPos = getPosition();
            Position newPos = currentPos.move(0, direction);
            if (canMoveTo(newPos)) {
                setPosition(newPos);
            } else {
                direction = -direction;
            }
        }
    }

    private void applyGravity() {
        Position currentPos = getPosition();
        Position below = currentPos.down();
        
        // Si Goomba está fuera del tablero, muere
        if (!game.isInside(currentPos)) {
            dead();
            return;
        }
        
        // Si below está fuera del tablero, el Goomba debe morir
        if (!game.isInside(below)) {
            dead();
            return;
        }
        
        // Si puede moverse abajo (no hay sólido), cae
        if (!game.isSolid(below)) {
            isFalling = true;
            setPosition(below);
        } else {
            isFalling = false;
        }
    }

    //Double Dispatch: Cuando un Goomba interactua con Mario
    public boolean interactWith(Mario mario) {
        if (!isAlive() || !mario.isAlive()) {
            return false;
        }
        // Verifica si están en la misma posición
        if (isInPosition(mario.getPosition()) || mario.isInPosition(this.getPosition())) {
            // El Goomba delega la interacción a Mario
            return mario.receiveInteraction(this);
        }
        return false;
    }

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

   @Override
    public boolean receiveInteraction(Mario mario) {
        
        if (!mario.isInPosition(this.getPosition())) {
            return false;
        }
        
        // El Goomba siempre muere
        dead();
        game.addScore(100);
        
        // Mario recibe daño SOLO si NO está cayendo
        if (!mario.isFalling()) {
            if (mario.isBig()) {
                mario.setBig(false);
            } else {
                game.loseLife();
            }
        }
        
        return true;
    }


    private boolean canMoveTo(Position position) {
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        return game.isInside(position) && !game.isSolid(position);
    }

    @Override
    public String toString() {
        return "Goomba at " + pos.toString();
    }

}
