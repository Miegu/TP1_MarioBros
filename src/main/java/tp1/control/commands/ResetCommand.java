package tp1.control.commands;

import tp1.view.Messages;
import tp1.view.GameView;
import tp1.logic.GameModel;

public class ResetCommand extends AbstractCommand{

    //Forman parte de atributos de estado
    private static final String NAME = Messages.COMMAND_RESET_NAME;
    private static final String SHORTCUT = Messages.COMMAND_RESET_SHORTCUT;
    private static final String DETAILS = Messages.COMMAND_RESET_DETAILS;
    private static final String HELP = Messages.COMMAND_RESET_HELP;

    private int level;

    public ResetCommand(){
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = -1; // Valor por defecto, resetea al nivel actual
    }

    public ResetCommand(int level){
        super(NAME, SHORTCUT, DETAILS, HELP);
        this.level = level;
    }

    @Override
     public Command parse(String[] commandWords){
        if(commandWords != null && commandWords.length >= 1 && commandWords.length <= 2){
            // IMPORTANTE: Verificar que la primera palabra coincide con el comando
            if(matchCommandName(commandWords[0])) {
                if(commandWords.length == 1){
                    //Reset sin parametros
                    return new ResetCommand(); //Usa el nivel por defecto
                }else if(commandWords.length == 2){
                    // Reset con nivel
                    try{
                        int level = Integer.parseInt(commandWords[1]);
                        return new ResetCommand(level);
                    } catch (NumberFormatException e) {
                        return null; // Parámetro inválido
                    }
                }
            }
        }
        return null; // No coincide con el comando 
    }


    @Override
    public void execute(GameModel game, GameView view) {
        if (level == -1) {
            // Reset sin parámetros - resetea al nivel actual
            game.reset();
        } else {
            // Reset con nivel específico
            game.reset(level);
        }
        
        // Mostrar el juego después del reset
        view.showGame();
    }
}
