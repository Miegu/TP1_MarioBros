package tp1.control.commands;
import tp1.logic.*;
import tp1.view.*;

public abstract class AbstractCommand implements Command{
	//Clase común para TODOS los comandos
	
	protected String name;
	protected String shortcut;
	protected String details;
	protected String help;
	
	
	//Construxtor:
	protected AbstractCommand(String name, String shortcut, String details, String help) {
		this.name = name;
		this.shortcut = shortcut;
		this.details = details;
		this.help = help;
	}

	
	//Comparamos un string con los atributos reales de nuestro comand
	public boolean matchCommand(String word) {
		return word != null && (word.equalsIgnoreCase(name) || word.equalsIgnoreCase(shortcut));
		//ignoreCase para que no tenga en cuenta si son mayusculas o minusculas
	}
	
	
	public String helpText(){
		return this.help;
	}
	
	//No implementa execute ni parse porque eso depende del comando concreto o de NoParamsCommand.


	
}