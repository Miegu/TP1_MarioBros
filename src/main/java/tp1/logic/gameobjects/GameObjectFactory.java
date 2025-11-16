package tp1.logic.gameobjects;

import java.util.Arrays;

import tp1.logic.GameWorld;
import tp1.logic.Position;

public class GameObjectFactory {

    public static GameObjectNew parse(String[] objWords, GameWorld game) {

        if (objWords == null || objWords.length < 2) {
        	return null;
        }

        String coord = objWords[0];
        if (!coord.startsWith("(") || !coord.endsWith(")")) return null;

        String inside = coord.substring(1, coord.length() - 1);
        String[] rc = inside.split(",");

        if (rc.length != 2) return null;

        int row, col;
        try {
            row = Integer.parseInt(rc[0]);
            col = Integer.parseInt(rc[1]);
        } catch (NumberFormatException e) {
            return null;
        }

        Position pos = new Position(row, col);

        String type = objWords[1].toUpperCase();

        String attr1 = (objWords.length > 2) ? objWords[2].toUpperCase() : null;
        String attr2 = (objWords.length > 3) ? objWords[3].toUpperCase() : null;

        switch (type) {

            case "LAND":
            case "L":
                return new Land(game, pos);

            case "EXITDOOR":
            case "ED":
                return new ExitDoor(game, pos);

            case "GOOMBA":
            case "G": {
                Goomba g = new Goomba(game, pos);

                if (attr1 != null) {
                    Integer dir = parseDir(attr1);
                    if (dir == null) return null;
                    g.setDirection(dir);
                }
                return g;
            }

            case "MARIO":
            case "M": {
                Mario m = new Mario(game, pos);

                int dir = 1;        // RIGHT por defecto
                boolean big = true; // BIG por defecto

                String[] attrs = Arrays.copyOfRange(objWords, 2, objWords.length);

                for (String raw : attrs) {
                    String tok = raw.toUpperCase();

                    Integer d = parseDir(tok);
                    if (d != null) {
                        dir = d;
                        continue;
                    }

                    Boolean size = parseSize(tok);
                    if (size != null) {
                        big = size;
                        continue;
                    }

                    return null;
                }

                m.setDirection(dir);
                m.setBig(big);
                return m;
            }

            case "MUSHROOM":
            case "MU":
                return new Mushroom(game, pos);

            case "BOX":
            case "B": {
                boolean full = true; 
                
                if (attr1 != null) {
                    if (attr1.equals("FULL") || attr1.equals("F")) {
                        full = true;
                    } else if (attr1.equals("EMPTY") || attr1.equals("E")) {
                        full = false;
                    } else {
                        return null; 
                    }
                }

                return new Box(game, pos, full);
            }

            default:
                return null;
        }
    }

   
    private static Integer parseDir(String s) {
        switch (s) {
            case "LEFT":
            case "L": return -1;
            case "RIGHT":
            case "R": return 1;
            case "STOP":
            case "S": return 0;
            default: return null;
        }
    }

    private static Boolean parseSize(String s) {
        switch (s) {
            case "BIG":
            case "B": return true;
            case "SMALL":
            case "S": return false;
            default: return null;
        }
    }
}
