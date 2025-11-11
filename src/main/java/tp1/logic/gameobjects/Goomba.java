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
    
        // 1: Aplicar gravedad (el goomba solo cae verticalmente si está cayendo)
        applyGravity();
    
        // 2: Movimiento horizontal SOLO si NO está cayendo
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
        Position pos = getPosition();
        Position below = pos.down();

        //Si se sale del tablero muere
        if (!game.isInside(pos)) {
            dead();
            return;
        }
        // Si debajo está fuera del tablero, muere
        if (!game.isInside(below)) {
            setPosition(below);
            dead();
            return;
        }
        if (!game.isSolid(below)) {
            isFalling = true;
            setPosition(below);
        } else {
            isFalling = false;
        }
    }

    @Override
    public boolean receiveInteraction(Mario mario) {
        dead(); // Siempre muere al interactuar con Mario (caiga o no)
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