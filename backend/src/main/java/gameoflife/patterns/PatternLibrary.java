package gameoflife.patterns;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Catalogue de tous les patterns disponibles.
 *
 * Ajouter un pattern = ajouter un {@code add(new Pattern(...))} ci-dessous.
 * Aucune logique à écrire : le motif est décrit en "art ASCII".
 */
public class PatternLibrary {

    private static final Map<String, Pattern> PATTERNS = new LinkedHashMap<>();

    private static void add(Pattern pattern) {
        PATTERNS.put(pattern.id, pattern);
    }

    static {
        // ---------------------------------------------------------------
        // Basiques — petite grille 20x20
        // ---------------------------------------------------------------
        add(new Pattern("blinker", "Blinker", 20, 20,
                "OOO"));

        add(new Pattern("glider", "Glider", 20, 20,
                ".O.",
                "..O",
                "OOO"));

        add(new Pattern("block", "Block", 20, 20,
                "OO",
                "OO"));

        // ---------------------------------------------------------------
        // Oscillateurs — formes qui pulsent en boucle
        // ---------------------------------------------------------------
        add(new Pattern("pulsar", "Pulsar", 17, 17,
                "..OOO...OOO..",
                ".............",
                "O....O.O....O",
                "O....O.O....O",
                "O....O.O....O",
                "..OOO...OOO..",
                ".............",
                "..OOO...OOO..",
                "O....O.O....O",
                "O....O.O....O",
                "O....O.O....O",
                ".............",
                "..OOO...OOO.."));

        add(new Pattern("pentadecathlon", "Pentadécathlon", 20, 20,
                "..O....O..",
                "OO.OOOO.OO",
                "..O....O.."));

        // ---------------------------------------------------------------
        // Vaisseaux — se déplacent à travers la grille
        // ---------------------------------------------------------------
        add(new Pattern("lwss", "LWSS (léger)", 25, 25,
                ".O..O",
                "O....",
                "O...O",
                "OOOO."));

        add(new Pattern("mwss", "MWSS (moyen)", 25, 25,
                "...O..",
                ".O...O",
                "O.....",
                "O....O",
                "OOOOO."));

        add(new Pattern("hwss", "HWSS (lourd)", 25, 25,
                "...OO..",
                ".O....O",
                "O......",
                "O.....O",
                "OOOOOO."));

        // ---------------------------------------------------------------
        // Usines / Guns — grande grille pour laisser filer les gliders
        // ---------------------------------------------------------------
        add(new Pattern("gosper", "Gosper Gun", 50, 50,
                "........................O...........",
                "......................O.O...........",
                "............OO......OO............OO",
                "...........O...O....OO............OO",
                "OO........O.....O...OO..............",
                "OO........O...O.OO....O.O...........",
                "..........O.....O.......O...........",
                "...........O...O....................",
                "............OO......................"));

        // ---------------------------------------------------------------
        // Méthuselahs — minuscules mais évoluent longtemps et chaotiquement
        // ---------------------------------------------------------------
        add(new Pattern("rpentomino", "R-pentomino", 60, 60,
                ".OO",
                "OO.",
                ".O."));

        add(new Pattern("acorn", "Acorn", 60, 60,
                ".O.....",
                "...O...",
                "OO..OOO"));

        add(new Pattern("diehard", "Diehard", 30, 30,
                "......O.",
                "OO......",
                ".O...OOO"));
    }

    public static Pattern get(String id) {
        Pattern pattern = PATTERNS.get(id);
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern inconnu : " + id);
        }
        return pattern;
    }

    public static boolean exists(String id) {
        return PATTERNS.containsKey(id);
    }
}
