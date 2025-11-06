package tp1.logic.gameobjects;

import tp1.logic.Position;

public interface GameItem {
    boolean isSolid();
    boolean isAlive();
    boolean isInPosition(Position pos);
    
    // Double-dispatch principal
    boolean interactWith(GameItem item);
    
    // Métodos sobrecargados para double-dispatch
    boolean receiveInteraction(Land obj);
    boolean receiveInteraction(ExitDoor obj);
    boolean receiveInteraction(Mario obj);
    boolean receiveInteraction(Goomba obj);
    
}
