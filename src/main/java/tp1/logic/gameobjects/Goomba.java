package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;
import tp1.view.Messages;


public class Goomba extends GameObject {

    private Game game;
    private int direccion; // -1 izquierda, 1 derecha Wombat empieza hacia la izq
    private boolean vivo;

    public Goomba(Game game, Position pos) {
        super(pos);
        this.game = game;
        this.direccion = -1; //Empieza mirando a la izquierda
        this.vivo = true;
    }

    @Override
    public String getIcon() {
        return Messages.GOOMBA;
    }
    
    @Override
    public void update() {
        if(!vivo) return; //Si no esta vivo no hace nada duh

        //1: Movimiento horizontal
        Position newPos = pos.move(0, direccion);
        if(canMoveTo(newPos)){
            pos = newPos;
        }else{
            direccion = -direccion; //Cambia de direccion si se choca       
        }
        //Gravedad
        applyGravity();
    }

    private void applyGravity(){
        Position debajo = pos.move(1, 0);
        //Si no hay suelo cae
         while (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            pos = debajo;
            debajo = pos.move(1, 0);
        }
        //Si se sale del tablero muere
        if(!pos.isValidPosition()){
            die();
        }
    }

    public boolean receiveInteraction(Mario mario){
        die();
        return true;
    }
    private boolean canMoveTo(Position position){
        //Comprueba si la posicion es valida y no hay ningun Land en esa posicion
        return position.isValidPosition() && !game.getGameObjects().isSolid(position);
    }
    public void die(){
        vivo = false;
    }
    
    public boolean estaVivo(){
        return vivo;
    }
}
