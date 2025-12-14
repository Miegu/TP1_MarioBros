package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.ActionList;
import tp1.logic.GameWorld;
import tp1.logic.Position;

/**
 * Clase abstracta para objetos controlables por el jugador (Mario, Luigi, etc.).
 * 
 * Proporciona funcionalidad común para personajes jugables:
 *   - Sistema de cola de acciones pendientes
 *   - Procesamiento de acciones del jugador
 *   - Control de tamaño (grande/pequeño)
 *   - Protección contra colisiones múltiples en el mismo turno
 *   - Ciclo de actualización estándar
 *   - Manejo de daño y gravedad
 * 
 * Las subclases (Mario, Luigi, etc.) deben implementar:
 *   {@link #executeAction(Action)} - Cómo ejecutar cada acción específicamente
 *   {@link #performAutomaticMovement()} - Movimiento automático sin acciones del jugador
 *   {@link #getIcon()} - Representación visual
 *   {@link #serialize()} - Guardado del objeto
 */
public abstract class PlayableObject extends MovingObject{

    protected boolean big;
    protected ActionList pendingActions;
    protected boolean hasMovedThisTurn;
    protected boolean collidedThisTurn;  // Para evitar colisiones múltiples

    /**
     * Constructor con GameWorld y Position.
     * Inicializa el personaje como grande, sin acciones pendientes.
     * 
     * @param game El mundo del juego
     * @param pos La posición inicial del personaje
     */
    public PlayableObject(GameWorld game, Position pos) {
        super(game, pos);
        this.big = true;
        this.pendingActions = new ActionList();
        this.hasMovedThisTurn = false;
        this.collidedThisTurn = false;
    }

    /**
     * Constructor protegido sin parámetros para factory
     */
    protected PlayableObject() {
        super();
        this.big = true;
        this.pendingActions = new ActionList();
        this.hasMovedThisTurn = false;
        this.collidedThisTurn = false;
    }
    // ==================== CICLO DE ACTUALIZACIÓN ====================
    
    /**
     * Ciclo de actualización completo del personaje jugable.
     * 
     * Flujo:
     * 1. Resetear flags de estado
     * 2. Verificar posición válida (especial para personajes grandes)
     * 3. Realizar interacciones iniciales
     * 4. Procesar acciones del jugador
     * 5. Si no se ha movido, aplicar movimiento automático
     * 6. Verificar si está fuera del tablero después del movimiento
     */
    @Override
    public void update() {
        // Resetear flags
        collidedThisTurn = false;
        isFalling = false;
        hasMovedThisTurn = false;
        
        Position pos = getPosition();
        
        // Verificar posición válida
        if (!pos.isValidPosition()) {
            game.loseLife();
            return;
        }
        
        // Verificar si personaje grande se sale por arriba
        if (big && !game.isInside(pos.up())) {
            game.loseLife();
            return;
        }
        
        // Realizar interacciones iniciales (antes de procesar acciones)
        game.doInteractionsFrom(this);
        
        // 1. Procesar acciones pendientes del jugador
        boolean playerHasActions = !pendingActions.isEmpty();
        if (playerHasActions) {
            processPlayerActions();
        }
        
        // 2. Si no se ha movido, aplicar movimiento automático
        if (!hasMovedThisTurn) {
            performAutomaticMovement();
            game.doInteractionsFrom(this);
        }
        
        // 3. Verificar si está fuera después del movimiento
        if (!game.isInside(getPosition())) {
            game.loseLife();
        }
    }

    // ==================== PROCESAMIENTO DE ACCIONES ====================
    
    /**
     * Procesa todas las acciones pendientes del jugador en orden.
     * Cada acción se ejecuta a través de executeAction().
     */
    protected void processPlayerActions() {
        for (Action action : pendingActions.getActions()) {
            executeAction(action);
        }
        pendingActions.clear();
    }

    /**
     * Ejecuta una acción individual del jugador.
     * Cada personaje (Mario, Luigi) implementa su propia lógica.
     * 
     * Responsabilidades de la subclase:
     *   - Mover el personaje si la acción es horizontal (LEFT, RIGHT, UP)
     *   - Marcar hasMovedThisTurn = true si hay movimiento
     *   - Llamar a game.doInteractionsFrom(this) después de movimientos
     *   - Actualizar la dirección según corresponda
     * 
     * @param action La acción a ejecutar
     */
    protected abstract void executeAction(Action action);
    
    /**
     * Realiza el movimiento automático cuando el jugador no proporciona acciones.
     * 
     * Responsabilidades de la subclase:
     *   - Aplicar gravedad si es necesario
     *   - Continuar en la dirección actual si no está cayendo
     *   - Llamar a game.doInteractionsFrom(this) después de movimientos
     */
    protected abstract void performAutomaticMovement();

    // ==================== INTERACCIONES CON OTROS OBJETOS ====================
    
    /**
     * Primera fase del double-dispatch: el jugador intenta interactuar con otro objeto.
     * Marca que hubo interacción en este turno (para proteger contra colisiones múltiples).
     * 
     * @param other El otro objeto del juego
     * @return true si ocurrió una interacción, false en caso contrario
     */
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }
    
    /**
     * El jugador recibe daño de un enemigo (Goomba, etc.).
     * 
     * Lógica:
     *   - Si ya colisionó este turno, no recibe más daño
     *   - Si es grande, se hace pequeño (pero puede recibir daño nuevamente)
     *   - Si es pequeño, pierde una vida
     * 
     * IMPORTANTE: Este método resetea collidedThisTurn si se reduce de tamaño,
     * permitiendo colisiones múltiples en el mismo turno si va perdiendo tamaño.
     */
    public void receiveDamage() {
        if (collidedThisTurn) {
            return;  // Ya recibió daño este turno
        }
        
        collidedThisTurn = true;
        
        if (big) {
            setBig(false);
            // IMPORTANTE: Resetear para permitir otro daño en el mismo turno
            // si el personaje sigue colisionando (va perdiendo tamaño)
            collidedThisTurn = false;
        } else {
            game.loseLife();
        }
    }

    // ==================== IMPLEMENTAR TODOS LOS RECEIVEINTERACTION ====================
    
    /**
     * Recibe interacción de otro jugador (para futuros juegos multijugador).
     * Por defecto, no hace nada.
     */
    @Override
    public boolean receiveInteraction(PlayableObject player) {
        return false;
    }
    
    /**
     * Recibe interacción de un NPC (manejado específicamente en subclases).
     * Delega la lógica al NPC para centralizar la interacción.
     */
    @Override
    public boolean receiveInteraction(NPCObject npc) {
        // Cuando un NPC choca con el jugador, delegamos de vuelta al NPC
        // para que él decida qué hacer (centralizando la lógica de interacción)
        return npc.receiveInteraction(this);
    }
    
    /**
     * Recibe interacción de Land (terreno sólido).
     * Por defecto, no hace nada.
     */
    @Override
    public boolean receiveInteraction(Land land) {
        return false;
    }
    
    /**
     * Recibe interacción de ExitDoor (salida).
     * Por defecto, no hace nada (manejado en subclases).
     */
    @Override
    public boolean receiveInteraction(ExitDoor door) {
        return false;
    }
    
    /**
     * Recibe interacción de Box (cajas).
     * Por defecto, no hace nada.
     */
    @Override
    public boolean receiveInteraction(Box box) {
        return false;
    }

    // ==================== CONTROL DEL PERSONAJE ====================
    
    /**
     * Agrega una acción a la cola de acciones pendientes.
     * Será procesada en el próximo update().
     * 
     * @param action La acción que el jugador quiere ejecutar
     */
    public void addAction(Action action) {
        pendingActions.addAction(action);
    }
    
    /**
     * Verifica si el personaje ya fue dañado en este turno.
     * Usado por enemigos para evitar daño múltiple.
     * 
     * @return true si ya colisionó, false en caso contrario
     */
    public boolean hasCollidedThisTurn() {
        return collidedThisTurn;
    }
    
    /**
     * Verifica si el personaje es grande.
     * 
     * @return true si es Big Mario/Luigi, false si es Small
     */
    public boolean isBig() {
        return big;
    }
    
    /**
     * Establece el tamaño del personaje.
     * 
     * @param big true para hacerlo grande, false para pequeño
     */
    public void setBig(boolean big) {
        this.big = big;
    }
    // ==================== OCUPACIÓN DE ESPACIO ====================
    
    /**
     * Verifica si este personaje ocupa una posición dada.
     * 
     * Big Mario ocupa 2 casillas verticales: la actual y la de arriba.
     * Small Mario ocupa solo 1 casilla.
     * 
     * @param position La posición a verificar
     * @return true si el personaje ocupa esa posición
     */
    @Override
    public boolean isInPosition(Position position) {
        Position currentPos = getPosition();
        
        if (currentPos.equals(position)) {
            return true;
        }
        
        if (big) {
            Position above = currentPos.up();
            return above.equals(position);
        }
        
        return false;
    }
    
    /**
     * Verifica si el personaje puede moverse a una posición.
     * 
     * Para Big Mario: la posición debe ser transitable, y también
     * la posición superior debe ser transitable.
     * Para Small Mario: solo se verifica la posición.
     * 
     * @param position La posición a verificar
     * @return true si puede moverse allí, false en caso contrario
     */
    @Override
    protected boolean canMoveTo(Position position) {
        // Verificar posición principal
        if (!game.isInside(position) || game.isSolid(position)) {
            return false;
        }
        
        // Si es grande, verificar también la posición superior
        if (big) {
            Position above = position.up();
            if (!game.isInside(above) || game.isSolid(above)) {
                return false;
            }
        }
        
        return true;
    }
    
    // ==================== GESTIÓN DE CICLO DE VIDA ====================
    
    @Override
    public boolean canBeRemoved() {
        return false;  // El jugador nunca se elimina
    }
    
    @Override
    public void onAdded(GameWorld game) {
        // Cuando el personaje es añadido al juego, registrarse como el principal
        game.registerAsMain(this);
    }
    
    @Override
    public boolean isSolid() {
        return false;  // El jugador no es sólido
    }
    
    @Override
    protected void handleOutOfBounds() {
        // Cuando el personaje sale del tablero, pierde una vida
        game.loseLife();
    }
    
    // ==================== PATRÓN DE INTERACCIÓN (DOUBLE-DISPATCH) ====================
    
    /**
     * Indica si la colisión de este personaje es crítica.
     * Una colisión crítica detiene el bucle de interacciones.
     * 
     * El personaje es crítico si ya ha colisionado en este turno
     * (para detener que múltiples objetos lo dañen en el mismo turno).
     * 
     * @return true si la colisión es crítica, false en caso contrario
     */
    @Override
    public boolean isCriticalCollision() {
        return collidedThisTurn;
    }
    
    // ==================== MÉTODOS ABSTRACTOS ====================
    
    @Override
    public abstract String getIcon();
    
    @Override
    public abstract String serialize();

}
