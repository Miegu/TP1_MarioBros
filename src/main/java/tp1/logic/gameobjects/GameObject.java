package tp1.logic.gameobjects;

import tp1.exceptions.ObjectParseException;
import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public abstract class GameObject implements GameItem {

    private Position pos; // If you can, make it private.
    private boolean isAlive;
    protected GameWorld game;

    //Constructor con GameWorld y Position
    public GameObject(GameWorld game, Position pos) {
        this.isAlive = true;
        this.pos = pos;
        this.game = game;
    }

    //Constructor Necesario para GameObjectFactory
    protected GameObject() {
        this.isAlive = true;
        this.pos = null;
        this.game = null;
    }

    public GameObject parse(String[] objWords, GameWorld game) throws ObjectParseException {
        return null; // Implementación por defecto
    }

    /**
     * Parsea una posición en formato "(fila,col)" a un objeto Position.
     *
     * @param posStr String con formato "(fila,col)"
     * @param objWords Array completo de palabras para mensajes de error
     * @return Position parseada
     * @throws ObjectParseException si el formato es inválido
     */
    protected static Position parsePosition(String posStr, String[] objWords) throws ObjectParseException {
        try {
            String cleanPosStr = posStr.replace("(", "").replace(")", "");
            String[] parts = cleanPosStr.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new Position(row, col);
        } catch (NumberFormatException e) {
            // Crear excepción anidada con el posStr original
            ObjectParseException innerException = new ObjectParseException(
                    Messages.ERROR_INVALID_POSITION_STR.formatted(posStr),
                    e
            );
            throw new ObjectParseException(
                    Messages.ERROR_INVALID_OBJECT_POSITION.formatted(String.join(" ", objWords)),
                    innerException
            );
        } catch (Exception e) {
            ObjectParseException innerException = new ObjectParseException(
                    Messages.ERROR_INVALID_POSITION_FORMAT.formatted(posStr),
                    e
            );
            throw new ObjectParseException(
                    Messages.ERROR_INVALID_OBJECT_POSITION.formatted(String.join(" ", objWords)),
                    innerException
            );
        }
    }

    @Override
    public boolean isInPosition(Position p) {
        return this.pos != null && this.pos.equals(p);
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    public void dead() {
        this.isAlive = false;
    }

    public Position getPosition() {
        return pos;
    }

    protected void setPosition(Position newPos) {
        this.pos = newPos;
    }

    public boolean canBeRemoved() {
        return true;  // Por defecto si se puede eliminar
    }

    /**
     * Este método se llama cuando el objeto es añadido al juego. Por defecto no
     * hace nada, pero las subclases pueden sobreescribirlo si necesitan
     * realizar alguna acción especial al ser añadidas.
     */
    public void onAdded(GameWorld game) {
        // Implementación vacía por defecto
        // La mayoría de objetos no necesitan hacer nada especial
    }

    public abstract String getIcon();

    @Override
    public abstract boolean isSolid();

    //Las subclases pueden sobreescribirlo
    public void update() {
        //Implementación vacía, cada objeto se actualiza a su modo
    }

    // Movimiento del objeto en una direccion dada
    protected void move(Action dir) {
        if (dir != null && pos != null) {
            Position newPos = pos.move(dir);
            if (newPos != null && game != null && game.isInside(newPos)) {
                this.pos = newPos;
            }
        }
    }

    // Representacion en String del objeto
    @Override
    public abstract String toString();

    // Serializacion del objeto
    public abstract String serialize();

    //Cada subclase debe implementar su propio Interact With
    @Override
    public abstract boolean interactWith(GameItem other);

    // Implementaciones por defecto (cada subclase las personaliza)
    @Override
    public boolean receiveInteraction(ExitDoor obj) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Land obj) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Mario obj) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Goomba obj) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Mushroom mushroom) {
        return false;
    }

    @Override
    public boolean receiveInteraction(Box box) {
        return false;
    }

}
