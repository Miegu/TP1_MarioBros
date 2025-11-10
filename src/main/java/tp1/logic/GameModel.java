package tp1.logic;
/*La interface que define el contrato para las operaciones de game model
 * Usado por el controlador para interactuar con la logica del juego
 */
public interface GameModel {
    boolean isFinished();
    /*Para actualizar las estadisticas del juego */
    void update();
    void reset();
    /*Reseteo con nivel especifico */
    void reset(int level);
    void exit();
    /*Añadir una accion para Mario */
    void addAction(Action action);
}
