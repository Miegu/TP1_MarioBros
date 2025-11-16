package tp1.logic;
import tp1.logic.gameobjects.GameObjectNew;
public interface GameModel {
    boolean isFinished();
    void update();
    void reset();
    void reset(int level);
    void addAction(Action action);
    void exit();
    //para que addcommand pueda pedir al juego que añada un objeto sin romper encapsulacion
    void addGameObject(GameObjectNew obj);
    }
