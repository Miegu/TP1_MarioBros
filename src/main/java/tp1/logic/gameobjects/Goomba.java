package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;


public class Goomba extends GameObject {

    private final Game game;

    public Goomba(Game game, Position pos) {
        super(pos);
        this.game = game;
    }

    @Override
    public String getIcon() {
        return "🐻";
    }
    @Override
    public void update() {
    }
}
