package gameoflife.service;

import gameoflife.dto.GameState;
import gameoflife.model.GameOfLife;
import gameoflife.patterns.Pattern;
import gameoflife.patterns.PatternLibrary;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    /**
     * Charge un pattern prédéfini : renvoie sa grille (à la taille
     * recommandée) et le mode tore adapté.
     */
    public GameState preview(String patternId) {
        Pattern pattern = PatternLibrary.get(patternId);
        return new GameState(pattern.toGrid(), pattern.torus);
    }

    /**
     * Calcule la génération suivante à partir de la grille reçue.
     * Aucun état conservé côté serveur : chaque joueur envoie sa propre
     * grille et son propre mode tore.
     */
    public boolean[][] next(boolean[][] grid, boolean torus) {
        GameOfLife game = new GameOfLife(grid, torus);
        game.nextGeneration();
        return game.getGrid();
    }
}
