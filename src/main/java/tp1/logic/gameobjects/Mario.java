package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.GameItem;
import tp1.logic.GameObjectContainer;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mario extends MovingObject {

   
    private boolean big;
    private boolean facingRight; //true si mira a la derecha, false si mira a la izquierda
    private ActionList accionesPendientes;
    private boolean hasMovedThisTurn; //para saber si ya se ha movido en un turn0
    private boolean jumping; //true si ha usado UP en este turno

    
    //Constructor de Mario
    public Mario(GameWorld game, Position position) {
    	// dir = 1 ---> empieza mirando a la derecha
        super(game, position, 1);
        this.big = true;// por defecto mario grande
        this.facingRight = true;
        this.accionesPendientes = new ActionList();
        this.hasMovedThisTurn = false;
        this.jumping=false;
    }

    
    @Override
    public void update() {
        boolean playerHasActions = !accionesPendientes.isEmpty();
        isFalling = false;
        hasMovedThisTurn = false;
        jumping=false;
        
        //1. Primero procesamos las acciones
        if (playerHasActions) {
            processPlayerActions();
        }

        //2. Si no se ha movido con acciones--> mov automatico
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
        }

        //3.Comprueba si interactua con algo
        game.doInteractionsFrom(this);

    }

  
    // Procedamos las acciones del jugador (las q estan en lista accionespendientes)
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
            jumping=true;
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
    
    
    //Metodos para controlar las interaciion con otros objetos
    @Override
    public boolean interactWith(GameItem other) {
        boolean canInteract = other.isInPosition(this.pos);
        if (!canInteract) {
        	return false;
        }
        return other.receiveInteraction(this);
    }
    
    
    @Override
    public boolean receiveInteraction(Goomba goomba) {
    	//si esta cayendo sobre el goomba el goomba muere
        if (isFalling) {
            goomba.kill();
            game.addPoints(100);
            return true;
        }
        if (big) {
            //si mario era grande se hace pequeño y goomba muere
            big = false;
            goomba.kill();
            game.addPoints(100);
        } 
        else {
            // si mario era peque muere
            goomba.kill();
            game.marioDies();
        }
        return true;
    }

    
    @Override
    public boolean receiveInteraction(ExitDoor door) {
        game.marioExited();
        return true;
    }
    
    @Override
    public boolean receiveInteraction(Box box) {
        //reacciona si mario está saltando y la caja está llena
        if (!jumping) {
            return false;
        }
        if (!box.isFull()) {
        	//si la caja esta vacia no hacemos nada
            return false;
        }
        //posición encima de la caja (donde aparece el Mushroom)
        Position above = new Position(box.getPosition().getRow() - 1, box.getPosition().getCol());
        //comprobamos que es una posición válida y no sólida
        if (above.isValidPosition() && !game.getGameObjects().isSolid(above)) {
            game.getGameObjects().add(new Mushroom(game, above));
        }
        //caja se vacía y damos puntos
        box.changeBox(false);
        game.addPoints(50);
        return true;
    }

}