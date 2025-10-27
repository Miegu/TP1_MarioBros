package tp1.control.commands;

public abstract class NoParamsCommand extends AbstractCommand {

	public NoParamsCommand(String name, String shortcut, String details, String help) {
		super(name, shortcut, details, help);
	}

	@Override
	public Command parse(String[] commandWords) {
		//Comandos sin parametros: solo debe haber una palabra
		if(commandWords != null && commandWords.length == 1){
			// Usa el metodo heredado para verificar si coincide
			if(matchCommandName(commandWords[0])){
				return this; // devuelve el mismo comando
			}
		}
		return null; //No coincide con este comando
	}
}
