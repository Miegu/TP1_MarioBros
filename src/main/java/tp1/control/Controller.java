package tp1.control;

import java.util.Scanner;

import tp1.logic.Game;
import tp1.view.GameView;
/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private Game game;
	private GameView view;
	private Scanner scanner;

	public Controller(Game game, GameView view) {
		this.game = game;
		this.view = view;
		this.scanner = new Scanner(System.in);
	}


	/**
	 * Runs the game logic, coordinate Model(game) and View(view)
	 * 
	 */
	public void run() {
		view.showWelcome();

		boolean exit = false;
		
		//TODO fill your code: The main loop that displays the game, asks the user for input, and executes the action.
		while(!exit &&game.playerWins() && !game.playerLoses()){
			//Muestra el juego
			view.showGame();
			//Pide la accion al usuario
			System.out.print("Accion: ");
			String accion = scanner.nextLine().trim();

			//Procesar la accion
			exit = processCommand(accion);

		}
		
		
		view.showEndMessage();
		scanner.close();
	}
	/*
	 * Procesa la accion dada por el usuario
	 */
	private boolean processCommand(String accion) {
		if(accion.isEmpty()){
			//Comando vacio, no hacer nada
			return false;
		}

		String[] parts = accion.split("\\s+");
		String command = parts[0].toLowerCase();

		switch(command){
			case "help":
			case "h":
				showHelp();
				break;
			
			case "exit":
			case "e":
				System.out.println("Jugador sale del juego");
				return true;

			case "reset":
            case "r":
                handleReset(parts);
                return false;
			
			case "update":
            case "u":
            case "":
                // Por ahora no hace nada hasta implementar movimiento (Hito C)
                return false;

			case "action":
            case "a":
                // Por ahora no hace nada hasta implementar acciones (Hito D)
                System.out.println("Acciones no implementadas");
                return false;
			
			default:
				System.out.println("Comando no reconocido");
				break;
		}
		return false;
	}

	//Muestra la ayuda
	private void showHelp() {
		System.out.println("Comandos disponibles:");
		System.out.println("[a]ction [[R]IGHT | [L]EFT | [U]P | [D]OWN | [S]TOP]+: user performs actions");
        System.out.println("[u]pdate | \"\": user does not perform any action");
        System.out.println("[r]eset [numLevel]: reset the game to initial configuration if not numLevel else load the numLevel map");
        System.out.println("[h]elp: print this help message");
        System.out.println("[e]xit: exits the game");
	}
	
	private void handleReset(String[] parts) {
        if (parts.length > 1) {
            // Hay un nivel especificado
            try {
                int level = Integer.parseInt(parts[1]);
                game.reset(level);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid level number: " + parts[1]);
            }
        } else {
            // Reset sin nivel especificado
            game.reset();
        }
    }
}
