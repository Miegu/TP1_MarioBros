package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.GameItem;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObjectNew {
    
    public Land(Game game, Position pos) {
        super(game, pos);
    }

    @Override
    public String getIcon() {
        return Messages.LAND;
    }

    @Override
    public void update() {
        //no se actualiza
    }

    @Override
    public boolean isSolid() {
        return true;
    }



	//Land no tiene interacciones con nadie
	@Override
	public boolean interactWith(GameItem other) {
		return false;
		}

}
