package tp1.view;

/**
 * Interfaz que define el contrato para la vista (presentación) del juego.
 * 
 * La vista es responsable de:
 * - Mostrar el estado del juego al usuario
 * - Recibir entrada del usuario desde la consola
 * - Presentar mensajes de error y bienvenida
 * - Renderizar el tablero del juego
 */
public interface ViewInterface {
    // show methods

    public void showWelcome();

    public void showGame();

    public void showEndMessage();

    public void showError(String message);

    public void showMessage(String message);

    // get data from view methods
    public String[] getPrompt();
}
