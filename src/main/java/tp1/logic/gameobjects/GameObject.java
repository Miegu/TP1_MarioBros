package tp1.logic.gameobjects;

import tp1.logic.Position;

/**
 *
 */
public abstract class GameObject {

    private Position pos;

    //Constructor base para todos los objetos del juego
    protected GameObject(Position pos) {
        this.pos = pos;
    }

    //Obtiene la posicion actual del objeto
    public Position getPosition() {
        return pos;
    }

    //Cambia la posicion del objeto
    public void setPosition(Position position) {
        this.pos = position;
    }

    //Metodo abstracto que sera implementado por las subclases
    public abstract String getIcon();

    public void update() {
        // Default implementation (can be overridden by subclasses)
    }

    //Comprueba si el objeto esta en una posicion concreta
    public boolean isInPosition(Position position) {
        return this.pos.equals(position);
    }

    public boolean estaVivo() {
        return true; // Por defecto, los objetos estan vivos
    }

    // Representacion en String del objeto
    @Override
    public String toString() {
        return getClass().getSimpleName() + " en " + pos.toString();
    }
}
