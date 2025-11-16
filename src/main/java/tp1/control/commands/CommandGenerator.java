package tp1.control.commands;
//Como esta en el mismo paquete que los comandos no hace falta hacer import de cada clase
import java.util.Arrays;
import java.util.List;




//Va a buscar y devolver el comando correpondiente
//Creamos una lista de objetos comando-->para que pueda buscar en cada uno
//no necesitamos constructor pq no vamos a creae objetos de esta clase!! 
//CommandGenerator se usa como un contenedor de métodos estáticos, solo hay métodos globales que se pueden usar directamente desde la clase

public class CommandGenerator {
	
	
	  private static final List<Command> AVAILABLE_COMMANDS =  Arrays.asList(
		        new ActionCommand(),
		        new ResetCommand(),
		        new HelpCommand(),
		        new UpdateCommand(),
		        new ExitCommand(),
		        new AddObjectCommand()
			  );
	  
	  //Método que devuelve el comando que estamos buscando según lo q introduzca el usuario
	  public static Command parse(String[] userWords) {

	        if (userWords == null || userWords.length == 0) {
	            return new UpdateCommand();
	        }
	       
	        //recorremos nuestra lista de objetos comandos
	        for (Command c : AVAILABLE_COMMANDS) {
	        	Command founded=c.parse(userWords); // cada comando tiene su propio parse,y devuelve el comando si es el q buscamos
	            if (founded != null) {
	                return founded; 
	            }
	        }
	        return null;
	    }
	  
	  
	  //Invoca el método helpText() de cada subclase de Command. A su vez, es invocado por el método execute de la clase HelpCommand.
	  public static String commandHelp() {
	       //va a ser llamado por el execute de la clase helpcomand
		  //tenemos que unir todos los textos de ayuda de cada comando!-->creamos un stringBuilder

		  StringBuilder chain= new StringBuilder(); //Recuerda q java ya inclute el metodo stringbuilder --> funciona con append

		  for (Command c : AVAILABLE_COMMANDS) {
	         chain.append(c.helpText()).append(System.lineSeparator()); 
	            
	        }

	        return chain.toString(); //convertimos el objeto stringbuilder a texto!!
	  
	  }
}

