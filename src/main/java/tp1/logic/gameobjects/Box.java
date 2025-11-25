package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;
import tp1.exceptions.OffBoardException;

public class Box extends GameObject {
    
    private boolean isEmpty;
    
    public Box(GameWorld game, Position pos) {
        this(game, pos, false); // Por defecto llena
    }
    
    public Box(GameWorld game, Position pos, boolean isEmpty) {
        super(game, pos);
        this.isEmpty = isEmpty;
    }

    protected Box() {
        super();
        this.isEmpty = false;
    }
    
    @Override
    public String getIcon() {
        return isEmpty ? Messages.EMPTY_BOX : Messages.BOX;
    }
    
    @Override
    public boolean isSolid() {
        return true; // Siempre sólida
    }
    
    @Override
    public boolean interactWith(GameItem other) {
        return other.receiveInteraction(this);
    }
    
    @Override
    public boolean receiveInteraction(Mario mario) {
        // Si ya esta vacía, no hace nada
        if (isEmpty) {
            return false; 
        }
        
        // Verificar si Mario golpea desde abajo
        Position below = getPosition().down();

        if (mario.isInPosition(below)) {
            // Mario golpeó desde abajo
            spawnMushroom();
            isEmpty = true;
            game.addScore(50); // 50 puntos
            return true;
        }
        
        return false;
    }
    
    private void spawnMushroom() {
        // La seta aparece encima de la caja
        Position above = getPosition().up();

        if (game.isInside(above)) {
            Mushroom mushroom = new Mushroom(game, above);
            try {
                game.addObject(mushroom);
            } catch (OffBoardException e) {
            // No debería ocurrir, ya que verificamos isInside
            }
        }
    }
    
    @Override
    public GameObject parse(String[] objWords, GameWorld game) {
        // Formato: (fila,col) BOX [FULL|EMPTY|F|E]
        // o simplemente: (fila,col) B
        if (objWords.length < 2) return null;
        
        String type = objWords[1].toUpperCase();
        if (!type.equals("BOX") && !type.equals("B")) {
            return null;
        }
        
        Position pos = parsePosition(objWords[0]);
        if (pos == null) return null;
        
        boolean isEmpty = false;
        if (objWords.length >= 3) {
            String state = objWords[2].toUpperCase();
            if (state.equals("EMPTY") || state.equals("E")) {
                isEmpty = true;
            }
        }
        
        return new Box(game, pos, isEmpty);
    }
    
    private Position parsePosition(String posStr) {
        try {
            posStr = posStr.replace("(", "").replace(")", "");
            String[] parts = posStr.split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new Position(row, col);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean canBeRemoved() {
        return false; // Las cajas nunca se eliminan
    }
    @Override
    public void update() {
    }

    
    @Override
    public String toString() {
        return "Box at " + getPosition().toString() + (isEmpty ? " (empty)" : " (full)");
    }
}
