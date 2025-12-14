package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.exceptions.PositionParseException;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

/*
* Clase abstracta que representa todos los objetos del juego.
* Implementa la interfaz GameItem
* Proporciona funcionalidad comun para todos los objetos del juego.
* Control de posición
* Control de estado (vivo/muerto)
* Metodo para parsear posición desde String
* Metodo para serializar el objeto
*
* Todos los objetos concretos del juego deben extender esta clase e implementar:
*
* {@link #getIcon()} - Representación visual
* {@link #isSolid()} - Comportamiento de colisión
* {@link #interactWith(GameItem)} - Lógica de interacción
* {@link #toString()} - Descripción en formato texto
* {@link #parse(String[], GameWorld)} - Creación del objeto desde archivo
* {@link #serialize()} - Guardado del objeto en archivo
* 
*/
public abstract class GameObject implements GameItem {

    /** Referencia al mundo del juego para interacciones y consultas */
    protected GameWorld game;

    /** Posición actual de este objeto en el mundo del juego */
    private Position pos;
    
    /** Estado de vida de este objeto */
    private boolean isAlive;

    /**
     * Construye un GameObject con un mundo de juego y posición inicial.
     * El objeto se crea en estado vivo por defecto.
     * 
     * @param game El mundo del juego al que pertenece este objeto
     * @param pos La posición inicial de este objeto
     */
    public GameObject(GameWorld game, Position pos) {
        this.game = game;
        this.pos = pos;
        this.isAlive = true;
    }

    /**
     * Constructor protegido sin argumentos para el patrón Factory.
     * Crea un objeto no inicializado que necesita ser parseado.
     * Usado por {@link GameObjectFactory} para crear instancias prototipo.
     */
    protected GameObject() {
        this.game = null;
        this.isAlive = true;
        this.pos = null;
    }

    // ==================== GESTIÓN DE POSICIÓN ====================
    
    /**
     * Obtiene la posición actual de este objeto.
     * 
     * @return La posición actual
     */
    public Position getPosition() {
        return pos;
    }

    /**
     * Establece una nueva posición para este objeto.
     * 
     * @param newPos La nueva posición
     */
    protected void setPosition(Position newPos) {
        this.pos = newPos;
    }
    
    /**
     * Determina si este objeto está en una posición dada.
     * 
     * @param p La posición a verificar
     * @return true si el objeto está en la posición dada, false en caso contrario
     */
    @Override
    public boolean isInPosition(Position p) {
        return this.pos != null && this.pos.equals(p);
    }

    // ==================== GESTIÓN DE ESTADO DE VIDA ====================
    
    @Override
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Marca este objeto como muerto.
     * Los objetos muertos típicamente se eliminan del juego en el siguiente ciclo de actualización.
     */
    public void dead() {
        this.isAlive = false;
    }
    /**
     * Determina si este objeto puede ser eliminado del juego.
     * Por defecto, los objetos pueden ser eliminados cuando están muertos.
     * 
     * @return true si el objeto puede ser eliminado, false en caso contrario
     */
    public boolean canBeRemoved() {
        return true;  // Por defecto si se puede eliminar
    }

    // ==================== CICLO DE VIDA DEL JUEGO ====================
    
    /**
     * Actualiza el estado de este objeto para un ciclo de juego.
     * Por defecto, no hace nada. Las subclases sobrescriben para implementar comportamiento.
     */
    public void update() {
        // Por defecto: sin lógica de actualización
    }

    /**
     * Se llama cuando este objeto se añade al mundo del juego.
     * Por defecto, no hace nada. Las subclases pueden sobrescribir para inicialización.
     * 
     * @param game El mundo del juego al que se añadió este objeto
     */
    public void onAdded(GameWorld game) {
        // Por defecto: sin acción especial al añadir
    }

    
    // ==================== MÉTODOS ABSTRACTOS ====================
    
    /**
     * Obtiene el icono visual que representa este objeto.
     * 
     * @return El icono en formato string para mostrar
     */
    public abstract String getIcon();

    /**
     * Determina si este objeto bloquea el movimiento (es sólido).
     * 
     * @return true si es sólido, false en caso contrario
     */
    @Override
    public abstract boolean isSolid();

    /**
     * Define cómo este objeto interactúa con otro elemento del juego.
     * Usa patrón de doble despacho para interacciones polimórficas.
     * 
     * @param other El otro elemento del juego con el que interactuar
     * @return true si la interacción ocurrió, false en caso contrario
     */
    @Override
    public abstract boolean interactWith(GameItem other);

    /**
     * Devuelve una descripción en string de este objeto.
     * 
     * @return Representación en string incluyendo tipo y posición
     */
    @Override
    public abstract String toString();

    /**
     * Parsea un array de strings en una instancia de objeto del juego.
     * Devuelve null si este parser no reconoce el formato.
     * 
     * @param objWords Array de palabras describiendo el objeto
     * @param game El mundo del juego para el nuevo objeto
     * @return Una nueva instancia de GameObject, o null si el formato no es reconocido
     * @throws ObjectParseException si el formato es reconocido pero inválido
     */
    public abstract GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException;

    /**
     * Serializa este objeto a formato string para guardado.
     * 
     * @return Representación en string para almacenamiento en archivo
     */
    public abstract String serialize();

     // ==================== RECEPTORES DE INTERACCIÓN POR DEFECTO ====================
    
    /**
     * Comportamiento por defecto al recibir interacción de ExitDoor.
     * Sobrescribir en subclases que reaccionen a ExitDoor.
     * 
     * @param obj El objeto ExitDoor
     * @return false por defecto (sin interacción)
     */
    @Override
    public boolean receiveInteraction(ExitDoor obj) {
        return false;
    }

    /**
     * Comportamiento por defecto al recibir interacción de Land.
     * Sobrescribir en subclases que reaccionen a Land.
     * 
     * @param obj El objeto Land
     * @return false por defecto (sin interacción)
     */
    @Override
    public boolean receiveInteraction(Land obj) {
        return false;
    }

    /**
     * Comportamiento por defecto al recibir interacción de Box.
     * Sobrescribir en subclases que reaccionen a Box.
     * 
     * @param box El objeto Box
     * @return false por defecto (sin interacción)
     */
    @Override
    public boolean receiveInteraction(Box box) { 
        return false; 
    }

    /**
     * Comportamiento por defecto al recibir interacción de player.
     * Sobrescribir en subclases que reaccionen a player.
     * 
     * @param player El objeto Player
     * @return false por defecto (sin interacción)
     */
    @Override
    public boolean receiveInteraction(PlayableObject player) {
        return false;
    }

    /**
     * Comportamiento por defecto al recibir interacción de npc
     * Sobrescribir en subclases que reaccionen a npc
     * 
     * @param npc El objeto npc
     * @return false por defecto (sin interacción)
     */
    @Override
    public boolean receiveInteraction(NPCObject npc) {
        return false;
    }
    
    /**
     * Indica si una colisión con este objeto es crítica (causa daño).
     * Por defecto, no lo es. Sobrescribir en subclases que representen peligro.
     * 
     * @return true si la colisión es crítica, false en caso contrario
     */
    @Override
    public boolean isCriticalCollision() {
        return false;
    }


    // ==================== UTILIDADES DE PARSING ====================
    
    /**
     * Parsea elementos comunes a todos los objetos del juego.
     * Valida el formato básico y parsea la posición.
     * 
     * <p>Este método es usado por las subclases para evitar duplicación.
     * Verifica:
     * <ul>
     *   <li>Número mínimo de argumentos (2)</li>
     *   <li>Tipo de objeto coincide con los esperados</li>
     *   <li>Posición válida y dentro del tablero</li>
     * </ul>
     * 
     * @param objWords Array de palabras que describen el objeto
     * @param expectedTypes Tipos válidos para este objeto (ej: "LAND", "L")
     * @return La posición parseada, o null si el tipo no coincide
     * @throws ObjectParseException si el formato es incorrecto
     */
    protected Position parseCommon(String[] objWords, String... expectedTypes) 
            throws ObjectParseException {
        
        // Validar número mínimo de argumentos
        if (objWords.length < 2) {
            return null; // No hay suficientes argumentos, no es este objeto
        }

        // Validar que el tipo coincide
        if (!matchesType(objWords[1], expectedTypes)) {
            return null; // No es este tipo de objeto
        }

        // Parsear y validar posición
        return parsePosition(objWords[0], objWords);
    }

    /**
     * Parsea un string de posición en formato "(fila,columna)" a un objeto Position.
     * Esta es una utilidad común usada por todos los parsers de objetos del juego.
     * 
     * @param posStr El string de posición a parsear (ej. "(3,5)")
     * @param objWords La descripción completa del objeto (para mensajes de error)
     * @return El objeto Position parseado
     * @throws ObjectParseException si el formato de posición es inválido o está fuera de límites
     */
    protected static Position parsePosition(String posStr, String[] objWords) throws ObjectParseException {
        try {
            // DELEGAR a Position.parsePosition() en lugar de duplicar código
            Position pos = Position.parsePosition(posStr);
            // Eliminar paréntesis y dividir por coma
            if (!pos.isValidPosition()) {
                throw new ObjectParseException(
                Messages.ERROR_OBJECT_POSITION_OFF_BOARD.formatted(String.join(" ", objWords))
                );
            }
            return pos;
        } catch (PositionParseException e) {
            String fullDescription = String.join(" ", objWords);
            throw new ObjectParseException(
                Messages.ERROR_INVALID_POSITION.formatted(fullDescription),
                e
            );
        }
    }

    /**
     * Valida que el tipo de objeto coincide con uno de los tipos esperados.
     * Este es un método de utilidad para reducir duplicación de código en los métodos parse.
     * 
     * @param actualType El string de tipo del input
     * @param expectedTypes Nombres de tipos válidos (ej: "MARIO", "M")
     * @return true si el tipo coincide con algún tipo esperado (ignorando mayúsculas/minúsculas)
     */
    protected static boolean matchesType(String actualType, String... expectedTypes) {
        String upperType = actualType.toUpperCase();
        for (String expected : expectedTypes) {
            if (upperType.equals(expected.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Valida que no haya demasiados argumentos para un objeto.
     * 
     * @param objWords Array de palabras del objeto
     * @param maxArgs Número máximo de argumentos permitidos
     * @throws ObjectParseException si hay demasiados argumentos
     */
    protected static void validateMaxArgs(String[] objWords, int maxArgs) 
            throws ObjectParseException {
        if (objWords.length > maxArgs) {
            throw new ObjectParseException(
                Messages.ERROR_OBJECT_PARSE_TOO_MANY_ARGS.formatted(String.join(" ", objWords))
            );
        }
    }
}
