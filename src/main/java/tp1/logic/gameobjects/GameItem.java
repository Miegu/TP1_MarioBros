package tp1.logic.gameobjects;

import tp1.logic.Position;

/**
 * Interfaz que define el contrato para todos los elementos del juego.
 * 
 * Proporciona:
 *   Consultas básicas de estado (sólido, vivo, posición)
 *   Sistema de interacciones mediante double-dispatch
 * 
 * Patrón Double-Dispatch:
 *   {@link #interactWith(GameItem)} - Primera dispatch: objeto A interactúa con B
 *   {@link #receiveInteraction(Mario)} y similares - Segunda dispatch: B reacciona según su tipo
 * 
 * Este patrón permite que dos objetos determinen dinámicamente cómo interactuar
 * basándose en los tipos de ambos objetos en tiempo de ejecución.
 */
public interface GameItem {
    // ==================== CONSULTAS DE ESTADO ====================
    boolean isSolid();

    boolean isAlive();

    boolean isInPosition(Position pos);

    // ==================== SISTEMA DE INTERACCIONES (DOUBLE-DISPATCH) ====================
    /**
     * Primera fase del double-dispatch: este objeto intenta interactuar con otro.
     * Delega al método receiveInteraction() del otro objeto para determinar el comportamiento.
     * 
     * Ejemplo de flujo:
     * mario.interactWith(goomba);
     *   → goomba.receiveInteraction(mario);
     *     → determina qué hacer según tipos de ambos
     * 
     * @param other El otro objeto del juego con el que interactuar
     * @return true si ocurrió una interacción, false en caso contrario
     */
    boolean interactWith(GameItem other);

    // Segunda fase del double-dispatch: 
    // Métodos sobrecargados para manejar las interacciones especificas
    boolean receiveInteraction(Land obj);
    
    boolean receiveInteraction(ExitDoor obj);

    boolean receiveInteraction(Mario obj);

    boolean receiveInteraction(Goomba obj);

    boolean receiveInteraction(Mushroom mushroom); 
    
    boolean receiveInteraction(Box box); 

    boolean isCriticalCollision();
}
