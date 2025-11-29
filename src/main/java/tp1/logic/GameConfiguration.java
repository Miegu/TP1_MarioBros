package tp1.logic;

import java.util.List;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Mario;

public interface GameConfiguration {
	//Estado del juego
	int getRemainingTime();
	int getPoints();
	int getNumLives();

    // Objetos del juego
	Mario getMario();                   
	List<GameObject> getNPCObjects();  //lista con el resto de gameobject
}
