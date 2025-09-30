package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;


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
        return "🐻";
    }
    
    @Override
    public void update() {
        if(!vivo) return; //Si no esta vivo no hace nada
        //1: Movimiento horizontal
        Position newPos = pos.move(direccion, 0);
        if(canMoveTo(newPos)){
            pos = newPos;
        }else{
            direccion = -direccion; //Cambia de direccion si se choca       
        }
        //Gravedad
        applyGravity();
    }
    private void applyGravity(){
        Position debajo = pos.move(0, 1);
        //Si no hay suelo cae
         while (debajo.isValidPosition() && !game.getGameObjects().isSolid(debajo)) {
            pos = debajo;
            debajo = pos.move(0, 1);
        }
        //Si se sale del tablero muere
        if(!pos.isValidPosition()){
            die();
        }
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
