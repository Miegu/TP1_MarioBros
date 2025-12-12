package tp1.logic;

import java.io.FileWriter;
import java.io.IOException;

import tp1.exceptions.GameLoadException;
import tp1.exceptions.GameModelException;
import tp1.exceptions.OffBoardException;
import tp1.logic.gameobjects.Box;
import tp1.logic.gameobjects.ExitDoor;
import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.Goomba;
import tp1.logic.gameobjects.Land;
import tp1.logic.gameobjects.Mario;
import tp1.logic.gameobjects.Mushroom;
import tp1.view.Messages;

/**
 * Implementación principal del modelo del juego Mario Bros.
 * 
 * Responsabilidades:
 * - Gestionar el estado del juego (tiempo, puntos, vidas)
 * - Administrar los objetos del tablero
 * - Procesar actualizaciones por turno
 * - Verificar condiciones de victoria/derrota
 * - Manejar persistencia (guardado/carga)
 * 
 * Patrón:
 * - Implements GameModel (interfaz), GameStatus (consultas), GameWorld (entorno)
 * - Inicialización por niveles (switch en initLevel)
 * - Delegación en GameObjectContainer para operaciones de objetos
 * 
 */
public class Game implements GameModel, GameStatus, GameWorld {

    // ===== CONSTANTES DEL TABLERO =====
    public static final int DIM_X = 30; // Ancho del tablero
    public static final int DIM_Y = 15; // Alto del tablero

    // ===== VALORES POR DEFECTO =====
    private static final int DEFAULT_TIME = 100;
    private static final int DEFAULT_LIVES = 3;
    private static final int DEFAULT_POINTS = 0;

    // ===== ESTADO DEL JUEGO =====
    private int nLevel;              // Nivel actual
    private int remainingTime;       // Tiempo restante en este nivel
    private int points;              // Puntuación acumulada
    private int lives;               // Vidas restantes

    // ===== CONFIGURACIÓN INICIAL (desde archivo) =====
    private int initialTime;         // Tiempo inicial del nivel para reset
    private int initialLives;        // Vidas iniciales del nivel para reset

    // ===== FLAGS DE ESTADO =====
    private boolean playerWon;       // Victoria (llegó a la puerta)
    private boolean playerLost;      // Derrota (sin tiempo o sin vidas)
    private boolean playerExit;      // Salida voluntaria

    // ===== OBJETOS DEL JUEGO =====
    private GameObjectContainer gameObjects;  // Contenedor de todos los objetos
    private Mario mario;                      // Referencia rápida a Mario
    private GameConfiguration fileLoader;     // Para cargar configuración de archivo
    private String currentFileName;         // Nombre del archivo cargado

    /**
     * Crea una nueva instancia del juego en un nivel específico.
     * 
     * Inicializa el estado básico e invoca initLevel para cargar el nivel.
     * 
     * @param nLevel Nivel a cargar (0= Un Goomba, 1=normal, 2=difícil, -1=creativo)
     */
    public Game(int nLevel) {
        this.nLevel = nLevel;
        this.remainingTime = DEFAULT_TIME;
        this.points = DEFAULT_POINTS;
        this.lives = DEFAULT_LIVES;
        this.initialTime = DEFAULT_TIME;
        this.initialLives = DEFAULT_LIVES;
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;
        this.currentFileName = null;

        initLevel(nLevel);
    }

    /**
     * Establece la configuración inicial del juego desde el archivo.
     * Este método es llamado por FileGameConfiguration al cargar.
     * 
     * @param time Tiempo inicial del nivel
     * @param lives Vidas iniciales del nivel
     */
    public void setInitialConfiguration(int time, int lives) {
        this.initialTime = time;
        this.initialLives = lives;
        
        // Aplicar también al estado actual
        this.remainingTime = time;
        this.lives = lives;
    }

    // ===== IMPLEMENTACIÓN GAMESTATUS =====

    @Override
    public int points() {
        return points;
    }

    @Override
    public int numLives() {
        return lives;
    }

    @Override
    public int remainingTime() {
        return remainingTime;
    }

    @Override
    public String positionToString(int col, int row) {
        Position position = new Position(row, col);
        return gameObjects.positionToString(position);
    }

    /**
     * Consulta si Mario ha ganado (llegó a la puerta).
     * @return true si Mario alcanzó la puerta de salida
     */
    public boolean playerWins() {
        return playerWon;
    }

    /**
     * Consulta si Mario ha perdido (sin tiempo o sin vidas).
     * @return true si se acabó el tiempo o las vidas
     */
    public boolean playerLoses() {
        return playerLost;
    }

    // ===== IMPLEMENTACIÓN GAMEWORLD =====

    @Override
    public int getRows() {
        return DIM_Y;
    }

    @Override
    public int getCols() {
        return DIM_X;
    }

    @Override
    public boolean isInside(Position pos) {
        if (pos == null) return false;
        return isValidCol(pos.getCol()) && isValidRow(pos.getRow());
    }

    /**
     * Verifica si una columna está dentro del tablero.
     * @param col Columna a verificar
     * @return true si 0 <= col < DIM_X
     */
    private boolean isValidCol(int col) {
        return col >= 0 && col < DIM_X;
    }

    /**
     * Verifica si una fila está dentro del tablero.
     * @param row Fila a verificar
     * @return true si 0 <= row < DIM_Y
     */
    private boolean isValidRow(int row) {
        return row >= 0 && row < DIM_Y;
    }

    @Override
    public boolean isSolid(Position pos) {
        return gameObjects.isSolid(pos);
    }

    @Override
    public void addObject(GameObject obj) throws OffBoardException {
        validateObjectPosition(obj);
        gameObjects.add(obj);
        obj.onAdded(this);
    }

    /**
     * Valida que la posición del objeto esté dentro del tablero.
     * 
     * @param obj Objeto a validar
     * @throws OffBoardException Si la posición está fuera del tablero
     */
    private void validateObjectPosition(GameObject obj) throws OffBoardException {
        if (!isInside(obj.getPosition())) {
            throw new OffBoardException(
                Messages.ERROR_POSITION_OFF_LIMITS + obj.getPosition()
            );
        }
    }

    @Override
    public boolean removeObjectAt(Position pos) {
        return gameObjects.removeObjectAt(pos);
    }

    @Override
    public GameObject getObjectAt(Position pos) {
        return gameObjects.getObjectAt(pos);
    }

    @Override
    public void addScore(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    @Override
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            playerLost = true;
        } else {
            // Reiniciar el nivel pero mantener puntos y vidas
            resetLevelKeepingProgress();
        }
    }

    /**
     * Reinicia el nivel actual preservando la puntuación y vidas.
     * Útil cuando Mario pierde una vida.
     */
    private void resetLevelKeepingProgress() {
        int currentPoints = this.points;
        int currentLives = this.lives;
        initLevel(this.nLevel);
        this.points = currentPoints;
        this.lives = currentLives;
    }

    @Override
    public void marioReachedExit() {
        // Bonificación: 10 puntos por cada segundo restante
        points += remainingTime * 10;
        remainingTime = 0;
        playerWon = true;
    }

    @Override
    public void doInteractionsFrom(GameItem item) {
        gameObjects.doInteraction(item);
    }

    @Override
    public void registerAsMain(GameObject obj) {
        // Mario se registra a sí mismo con el juego
        this.mario = (Mario) obj;
    }

    // ===== IMPLEMENTACIÓN GAMEMODEL =====

    @Override
    public void update() {
        // 1. Reducir tiempo
        decreaseTime();
        
        // 2. Actualizar todos los objetos
        gameObjects.update();
    }

    /**
     * Reduce el tiempo disponible y marca como perdedor si se agota.
     */
    private void decreaseTime() {
        if (remainingTime <= 0) {
            playerLost = true;
        } else {
            remainingTime--;
        }
    }

    @Override
    public boolean isFinished() {
        return playerWon || playerLost || playerExit;
    }

    @Override
    public void reset() {
        if(currentFileName != null) {
            try {
                load(currentFileName);
            } catch (GameLoadException e) {
                // Si falla la carga, continuar con el reinicio normal
                reset(this.nLevel);
            }
        }else{
            //Si no hay archivo cargado, reiniciar el nivel actual
            reset(this.nLevel);
        }
    }

    @Override
    public void reset(int level) {
        // limpiar estado de archivo cargado si se reinicia con nivel específico
        this.currentFileName = null;

        // Reiniciar flags de estado
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;
        this.nLevel = level;

        //Recargar el nivel
        initLevel(level);

        // Solo resetear puntos si es nivel -1 (creativo)
        if (level == -1) {
            this.points = DEFAULT_POINTS;
        }
    }

    @Override
    public void addAction(Action action) {
        if (mario != null) {
            mario.addAction(action);
        }
    }

    @Override
    public void exit() {
        this.playerExit = true;
    }

    // ===== MANEJO FICHEROS =====

    @Override
    public void load(String fileName) throws GameLoadException {
        // Guardar el nombre del archivo cargado para recargar en reset
        this.currentFileName = fileName;

        GameConfiguration cfg = new FileGameConfiguration(fileName, this);

        // Reiniciar flags de estado
        resetGameFlags();

        // Cargar valores del archivo
        loadGameStateFromConfiguration(cfg);

        // Cargar objetos
        loadGameObjects(cfg);

        this.fileLoader = cfg;
    }

    /**
     * Reinicia todas las flags de estado del juego.
     * Útil antes de cargar un archivo.
     */
    private void resetGameFlags() {
        this.playerWon = false;
        this.playerLost = false;
        this.playerExit = false;
    }

    /**
     * Carga los valores de estado desde la configuración.
     * @param cfg Configuración cargada del archivo
     */
    private void loadGameStateFromConfiguration(GameConfiguration cfg) {
        int time = cfg.getRemainingTime();
        int lives = cfg.getNumLives();

        setInitialConfiguration(time, lives);

        this.points = cfg.getPoints();
    }

    /**
     * Carga los objetos del juego desde la configuración.
     * @param cfg Configuración cargada del archivo
     */
    private void loadGameObjects(GameConfiguration cfg) {
        this.gameObjects = new GameObjectContainer();

        // Cargar Mario primero (para que sea el primero en actualizar)
        this.mario = cfg.getMario();
        if (this.mario != null) {
            this.gameObjects.add(this.mario);
        }

        // Cargar el resto de objetos
        for (GameObject obj : cfg.getNPCObjects()) {
            this.gameObjects.add(obj);
        }
    }

    @Override
    public void save(String fileName) throws GameModelException {
        FileWriter fw = null;

        try {
            //Abrimos el fichero
            fw = new FileWriter(fileName);

            //Escribimos la info del juego
            fw.write(this.toString());

        } catch (IOException e) {
            //Cualquier problema de escritura lo convertimos en GameModelException
            throw new GameModelException("El sistema no puede encontrar el archivo especificado: " + fileName, e);

        } finally {
            //Cerrar el fichero si estaba abierto
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignore) {
                } //ignoramos error
            }
        }
    }

    // ===== REPRESENTACIÓN EN STRING =====

    /**
     * Serializa el estado del juego para guardarlo en archivo.
     * 
     * Formato:
     * - Línea 1: tiempo puntos vidas
     * - Línea 2+: cada objeto vivo en formato serializado
     * 
     * @return String con el estado completo del juego
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Encabezado: estado del juego
        appendGameHeader(sb);
        
        // Cuerpo: objetos vivos
        appendGameObjects(sb);
        
        return sb.toString();
    }

    /**
     * Añade el encabezado al StringBuilder (tiempo, puntos, vidas).
     */
    private void appendGameHeader(StringBuilder sb) {
        sb.append(remainingTime).append(" ")
          .append(points).append(" ")
          .append(lives).append("\n");
    }

    /**
     * Añade todos los objetos vivos al StringBuilder.
     */
    private void appendGameObjects(StringBuilder sb) {
        for (GameObject obj : gameObjects.getObjects()) {
            if (obj.isAlive()) {
                sb.append(obj.serialize()).append("\n");
            }
        }
    }

    // ===== INICIALIZACIÓN DE NIVELES =====

    /**
     * Inicializa el nivel especificado.
     * Carga el mapa, enemigos, objetos, etc.
     * 
     * @param nLevel Nivel a cargar
     */
    private void initLevel(int nLevel) {
        switch (nLevel) {
            case -1 -> initLevelMinus1();  // Modo creativo
            case 0 -> initLevel0();         // Fácil
            case 1 -> initLevel1();         // Normal
            case 2 -> initLevel2();         // Difícil
            default -> initLevel0();        // Por defecto: fácil
        }
    }

    /**
     * Nivel -1: Modo creativo vacío.
     * El usuario puede añadir objetos manualmente.
     */
    private void initLevelMinus1() {
        this.nLevel = -1;
        this.remainingTime = DEFAULT_TIME;
        this.gameObjects = new GameObjectContainer();
        this.mario = null;
    }

    /**
     * Nivel 0: Nivel fácil (tutorial).
     * - Mario comienza en posición (12, 0)
     * - Terreno básico
     * - Un Goomba enemigo
     * - Puerta de salida
     */
    private void initLevel0() {
        this.nLevel = 0;
        this.remainingTime = DEFAULT_TIME;
        this.gameObjects = new GameObjectContainer();

        // 1. Añadir a Mario (primero para que se actualice primero)
        this.mario = new Mario(this, new Position(Game.DIM_Y - 3, 0));
        gameObjects.add(this.mario);

        // 2. Añadir terreno
        addTerrainLevel0();

        // 3. Añadir puerta de salida
        gameObjects.add(
            new ExitDoor(this, new Position(Game.DIM_Y - 3, Game.DIM_X - 1))
        );

        // 4. Añadir enemigos
        gameObjects.add(new Goomba(this, new Position(0, 19)));
    }

    /**
     * Añade el terreno para el nivel 0.
     */
    private void addTerrainLevel0() {
        // Terreno base (12 columnas)
        for (int col = 0; col < 15; col++) {
            gameObjects.add(new Land(this, new Position(13, col)));
            gameObjects.add(new Land(this, new Position(14, col)));
        }

        // Plataforma intermedia
        gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 9)));
        gameObjects.add(new Land(this, new Position(Game.DIM_Y - 3, 12)));

        // Terreno final
        for (int col = 17; col < Game.DIM_X; col++) {
            gameObjects.add(new Land(this, new Position(Game.DIM_Y - 2, col)));
            gameObjects.add(new Land(this, new Position(Game.DIM_Y - 1, col)));
        }

        // Plataformas adicionales
        addIntermediatePlatforms();

        // Salto final (escalera)
        addFinalJump();
    }

    /**
     * Añade plataformas intermedias en el nivel 0.
     */
    private void addIntermediatePlatforms() {
        gameObjects.add(new Land(this, new Position(9, 2)));
        gameObjects.add(new Land(this, new Position(9, 5)));
        gameObjects.add(new Land(this, new Position(9, 6)));
        gameObjects.add(new Land(this, new Position(9, 7)));
        gameObjects.add(new Land(this, new Position(5, 6)));
    }

    /**
     * Añade el salto final (escalera triangular) en el nivel 0.
     */
    private void addFinalJump() {
        int tamX = 8;
        int tamY = 8;
        int posIniX = Game.DIM_X - 3 - tamX;
        int posIniY = Game.DIM_Y - 3;

        for (int col = 0; col < tamX; col++) {
            for (int fila = 0; fila < col + 1; fila++) {
                gameObjects.add(
                    new Land(this, new Position(posIniY - fila, posIniX + col))
                );
            }
        }
    }

    /**
     * Nivel 1: Nivel normal.
     * Extiende el nivel 0 con más Goombas.
     */
    private void initLevel1() {
        initLevel0();  // Cargar nivel 0 primero
        this.nLevel = 1;

        // Añadir más Goombas
        gameObjects.add(new Goomba(this, new Position(4, 6)));
        gameObjects.add(new Goomba(this, new Position(12, 6)));
        gameObjects.add(new Goomba(this, new Position(12, 8)));
        gameObjects.add(new Goomba(this, new Position(10, 10)));
        gameObjects.add(new Goomba(this, new Position(12, 11)));
        gameObjects.add(new Goomba(this, new Position(12, 14)));
    }

    /**
     * Nivel 2: Nivel difícil.
     * Extiende el nivel 1 con Boxes y Mushrooms.
     */
    private void initLevel2() {
        initLevel1();  // Cargar nivel 1 primero
        this.nLevel = 2;

        // Añadir Box
        gameObjects.add(new Box(this, new Position(9, 4)));

        // Añadir Mushrooms
        gameObjects.add(new Mushroom(this, new Position(12, 8)));
        gameObjects.add(new Mushroom(this, new Position(2, 20)));
    }

}
