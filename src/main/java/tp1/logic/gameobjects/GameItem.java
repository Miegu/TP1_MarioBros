package tp1.logic.gameobjects;
import tp1.logic.Position;


public interface GameItem {
	boolean isSolid();
    boolean isAlive();
    boolean isInPosition(Position pos);
    boolean interactWith(GameItem item);
    boolean receiveInteraction(Land o);
    boolean receiveInteraction(ExitDoor o);
    boolean receiveInteraction(Mario o);
    boolean receiveInteraction(Goomba o);
    boolean receiveInteraction(Box o);
    boolean receiveInteraction(Mushroom o);
    
}
