package tp1.control.commands;

//Clase abstracta que hereda de AbstractCommand y que sirve de base para todos los comandos que no tienen parámetros
//Tiene que ser abstracta porque no se puede instanciar directamente, y no pasa nada si no implemento todos los métodos abstractos, porque las subclases lo harán.
public abstract class  NoParamsCommand extends AbstractCommand {
	
	
	//Cosntrcutor--> que llama al constructor de la clase abstarct comman (clase padre)
	//protected porque solo pueden usarlo las clases hijas
	protected NoParamsCommand(String name, String shortcut, String details, String help) {
        super(name, shortcut, details, help);
    }
	
	//Devuelve el tipo de command si coincide
		 @Override
    public Command parse(String[] words) {
        // tiene que haber exactamente UNA palabra, y que sea mi nombre
        if (words != null && words.length == 1 && matchCommand(words[0])) {
            return this;
        }
        return null;
    }

	
}
