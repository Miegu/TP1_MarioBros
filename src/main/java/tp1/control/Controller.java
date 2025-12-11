package tp1.control;

import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.exceptions.CommandException;
import tp1.logic.GameModel;
import tp1.view.GameView;
import tp1.view.Messages;

/**
 * Controlador principal del juego que coordina el flujo entre el modelo (lógica
 * del juego) y la vista (representación en consola).
 *
 * Implementa el patrón MVC (Model-View-Controller) actuando como intermediario:
 * - Procesa entrada del usuario mediante el patrón Command 
 * - Ejecuta acciones sobre el modelo del juego 
 * - Actualiza la vista según cambios en el modelo
 *
 * El flujo de control sigue este esquema: 
 * 1. Usuario introduce comando en la consola 
 * 2. View procesa entrada y devuelve array de palabras 
 * 3. CommandGenerator parsea el comando 
 * 4. Command se ejecuta sobre el GameModel
 * 5. View se actualiza para reflejar cambios
 *
 */
public class Controller {

    /**
     * Modelo del juego que contiene la lógica principal del juego.
     */
    private GameModel game;

    /**
     * Vista del juego responsable de mostrar información al usuario e obtener
     * entrada.
     */
    private GameView view;

    /**
     * Constructor que inicializa el controlador con el modelo y la vista.
     *
     * @param game Modelo del juego (contiene toda la lógica principal)
     * @param view Vista del juego (responsable de mostrar datos al usuario e
     * obtener entrada)
     */
    public Controller(GameModel game, GameView view) {
        this.game = game;
        this.view = view;
    }

    /**
     * Inicia y ejecuta el bucle principal del juego.
     *
     * Coordina el flujo entre el modelo y la vista en ciclos repetitivos: 
     * 1. Muestra pantalla de bienvenida 
     * 2. Muestra estado inicial del tablero 
     * 3. Entra en bucle principal donde: 
     *      - Obtiene entrada del usuario desde la vista 
     *      - Parsea la entrada en un comando usando CommandGenerator 
     *      - Ejecuta el comando sobre el modelo 
     *      - Captura excepciones de comando y las muestra al usuario 
     *      - Muestra los cambios en el modelo a través de la vista 
     * 4. Termina cuando el juego indica que ha finalizado 
     * 5. Muestra mensaje de fin de partida
     *
     * La gestión de excepciones incluye el manejo de causas encadenadas,
     * permitiendo mostrar errores de validación detallados al usuario.
     */
    public void run() {
        // Mostrar pantalla de bienvenida
        view.showWelcome();
        // Mostrar estado inicial del juego
        view.showGame();

        // Bucle principal del juego - continúa mientras el juego no esté terminado
        while (!game.isFinished()) {
            // Obtener entrada del usuario desde la vista (devuelve array de palabras)
            String[] words = view.getPrompt();

            // Si no hay entrada (array vacío), terminar el juego (por ejemplo, EOF)
            if (words.length == 0) {
                break;
            }
            try {
                // Mostrar entrada del usuario para propósitos de depuración
                System.out.println(Messages.DEBUG.formatted(String.join(" ", words)));

                // Parsear la entrada para obtener el comando correspondiente
                Command command = CommandGenerator.parse(words);
                // Ejecutar el comando sobre el modelo del juego
                command.execute(game, view);

            } catch (CommandException e) {
                // Muestra el mensaje principal del error
                view.showError(e.getMessage());

                // Mostrar todos los mensajes encadenados (causas anidadas)
                // Esto permite mostrar errores de validación detallados
                Throwable cause = e.getCause();
                while (cause != null) {
                    view.showError(cause.getMessage());
                    cause = cause.getCause();
                }
            }
        }
        // Mostrar mensaje de fin de juego
        view.showEndMessage();
    }
}
