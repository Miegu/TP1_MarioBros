package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.Game;
import tp1.logic.GameObjectContainer;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mario extends MovingObject {

   
    private boolean big;
    private boolean facingRight; //true si mira a la derecha, false si mira a la izquierda
    private ActionList accionesPendientes;
    private boolean hasMovedThisTurn; //para saber si ya se ha movido en un turn0

    
    //Constructor de Mario
    public Mario(Game game, Position position) {
    	// dir = 1 ---> empieza mirando a la derecha
        super(game, position, 1);
        this.big = true;// por defecto mario grande
        this.facingRight = true;
        this.accionesPendientes = new ActionList();
        this.hasMovedThisTurn = false;
    }

    
    @Override
    public void update() {
        boolean playerHasActions = !accionesPendientes.isEmpty();
        isFalling = false;
        hasMovedThisTurn = false;
        
        //1. Primero procesamos las acciones
        if (playerHasActions) {
            processPlayerActions();
        }

        //2. Si no se ha movido con acciones--> movimiento automatico
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
        }

        //3.Comprueba si interactua con algo (Wombat? MAybe)
        game.doInteractionsFrom(this);

    }

  
    // Porcedamos las acciones del jugador (las q estan en lista accionespendientes)
    private void processPlayerActions() {
        if (accionesPendientes.isEmpty()) {
            return;
        }

        for (Action action : accionesPendientes.getActions()) {
            executeAction(action);
        }

        accionesPendientes.clear();
    }
    

  
    
    // Ejecuta una acción det
    private void executeAction(Action action) {
        Position newPos;

        switch (action) {
        case LEFT:
            newPos = pos.move(action.getY(), action.getX());
            if (canMoveTo(newPos)) { 
                pos = newPos;
                hasMovedThisTurn = true;
            }
            dir = -1;         
            facingRight = false;
            break;

        case RIGHT:
            newPos = pos.move(action.getY(), action.getX());
            if (canMoveTo(newPos)) {
                pos = newPos;
                hasMovedThisTurn = true;
            }
            dir = 1;
            facingRight = true;
            break;

        case UP:
            newPos = pos.move(action.getY(), action.getX());
            if (canMoveTo(newPos)) {
                pos = newPos;
                hasMovedThisTurn = true;
            }
            break;

        case DOWN:
            if (isOnGround()) {
                dir = 0;    
            } else {
                while (!isOnGround()) {
                    applyGravity();  
                }
            }
            break;

        case STOP:
            dir = 0;
            break;
        }
    }

    
    
    // Movimiento automático
    private void performAutomaticMovement() {
        if (dir == 0) {
            return; // STOP
        }

        if (!isOnGround() && !hasMovedThisTurn) {
            applyGravity();
            return;
        }

        Position newPos = pos.move(0, dir);

        if (canMoveTo(newPos)) {
            pos = newPos;
        } else {
            dir = -dir; // Cambia de dirección si se choca
            facingRight = (dir == 1); // Actualiza hacia dónde mira
        }
    }

    //Interacciones:
    // Interacción con la puerta de salida
    public boolean interactWith(ExitDoor door) {
        if (this.pos.equals(door.getPosition())) {
            game.marioExited();
            return true;
        }
        return false;
    }
    
    //Interacción con Goomba
    public boolean interactWith(Goomba goomba) {

        if (!this.pos.equals(goomba.getPosition())) {
            return false;
        }

        //Para saber si esta cayendo encima del goomba
        if (isFalling) {
            goomba.receiveInteraction(this);
            game.addPoints(100);
            return true;
        }

        if (big) {
            //No muere pero se hace pequeño
            big = false;
            goomba.receiveInteraction(this); //el Goomba muere
            game.addPoints(100);
        } else {
            //Mario muere
            goomba.receiveInteraction(this);
            game.marioDies();
        }
        return true;
    }

    
    
    //Añadimos una acción a la lista de acciones pendientes
    public void addAction(Action action) {
        accionesPendientes.addAction(action);
    }
    
    // Acceso a los objetos del juego
    public GameObjectContainer getGameObjects() {
        return game.getGameObjects();
    }

    public boolean isBig() {
        return big;
    }

    public void setBig(boolean big) {
        this.big = big;
    }
    
    
    //Métodos sobreescritos:
    
    //Aplicamos gravedad (usamos hasmovedThisturn asi q tiene q estar sobreescrita)
    @Override
    protected void applyGravity() {
        Position debajo = pos.move(Action.DOWN.getY(), Action.DOWN.getX());

        if (!pos.isValidPosition()) {
            game.marioDies();
            return;
        }

        if (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            pos = debajo;
            isFalling = true;
            hasMovedThisTurn = true;
        }
    }
    
    //Icono de Mario según dirección y si es grande o no
    @Override
    public String getIcon() {
        if (dir == 0) {
            return Messages.MARIO_STOP;
        } else if (facingRight) {
            return Messages.MARIO_RIGHT;
        } else {
            return Messages.MARIO_LEFT;
        }
    }

    //Va a ser distinta tb pq tenemos q tener en cuenta si mario es grande o no
    @Override
    public boolean isInPosition(Position position) {
    	if (pos.equals(position)) {
            return true;
        }
        // Si Mario es grande tb ocupa la posición de arriba
        if (big && pos.getRow() > 0) {
            Position above = new Position(pos.getRow() - 1, pos.getCol());
            return above.equals(position);
        }
        return false;
    }
}