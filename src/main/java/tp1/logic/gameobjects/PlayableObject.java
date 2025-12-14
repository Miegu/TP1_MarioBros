package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class PlayableObject extends MovingObject{

    public PlayableObject(GameWorld game, Position pos){
        super(game, pos);
    }

    protected PlayableObject(){
        super();
    }



}
