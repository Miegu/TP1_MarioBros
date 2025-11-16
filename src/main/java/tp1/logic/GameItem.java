package tp1.logic;
import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.Box;
import tp1.logic.gameobjects.Mushroom;


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
