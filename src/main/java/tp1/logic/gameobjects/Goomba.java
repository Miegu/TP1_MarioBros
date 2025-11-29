package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goomba extends MovingObject {

    protected Goomba() {
        super();
    }

    public Goomba(GameWorld game, Position pos) {
        super(game, pos);
        this.direction = Action.LEFT; //Empieza mirando a la izquierda
    }

    @Override
    protected void handleOutOfBounds() {
        // Cuando el Goomba sale del tablero, muere
        dead();
    }
    public boolean isFalling() {
        return isFalling;
    }

    @Override
    public void update() {
        if (!vivo) {
            return; //Si no esta vivo no hace nada duh
        }        
        //1: Aplicar gravedad
        applyGravity();
        game.doInteractionsFrom(this);

        // 2: Horizontal movement if not falling
        if (!isFalling) {
            Position pos = getPosition();
            Position newPos = pos.move(0, direccion);
            if (canMoveTo(newPos)) {
                setPosition(newPos);
            } else {
                newPos = currentPos.right();
            }

            if(canMoveTo(newPos)){
                setPosition(newPos);
                game.doInteractionsFrom(this);
            }else{
                //Cambiar direccion si choca con algo
                direction = (direction == Action.LEFT) ? Action.RIGHT : Action.LEFT;
            }
        }
    }
    //METODOS DE INTERACCION

    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }

    private void applyGravity() {
        Position pos = getPosition();
        Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());
        //Si se sale del tablero muere
        if (!pos.isValidPosition()) {
            die();
        }
        if (!game.getGameObjects().isSolid(debajo)) {
            isFalling = true;
            setPosition(debajo);
        } else {
            isFalling = false;
        }

    @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }

    @Override
    public boolean isSolid() {
        return false;
    }


    private boolean canMoveTo(Position position) {
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        return position.isValidPosition() && !game.getGameObjects().isSolid(position);
    }

    private Position parsePosition(String posStr) {
        try {
        // Formato: (fila,columna)
        posStr = posStr.replace("(", "").replace(")", "");
        String[] parts = posStr.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        return new Position(row, col);
    } catch (Exception e) {
        return null;
    }
        
    }
    //serialize tiene q escribir (fila, col) goomba direccion
    @Override
    public String serialize() {
    	int row = getPosition().getRow();
        int col = getPosition().getCol();

    @Override
    public boolean estaVivo() {
        return vivo;
    }

}