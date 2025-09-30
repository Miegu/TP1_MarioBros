package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;


public class Goomba extends GameObject {

    private Game game;

    public Goomba(Game game, Position pos) {
        super(pos);
        this.game = game;
    }

    @Override
    public char getIcon() {
        return 'G';
    }
    @Override
    public void update() {
    }
}
