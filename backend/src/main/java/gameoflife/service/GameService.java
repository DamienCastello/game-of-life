package gameoflife.service;

import gameoflife.model.GameOfLife;
import gameoflife.patterns.PatternLibrary;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    /**
     * Charge un pattern prédéfini : renvoie sa grille à la taille recommandée.
     */
    public boolean[][] preview(String patternId) {
        return PatternLibrary.get(patternId).toGrid();
    }

    /**
     * Calcule la génération suivante à partir de la grille reçue.
     * Aucun état conservé côté serveur : chaque joueur envoie sa propre grille.
     */
    public boolean[][] next(boolean[][] grid) {
        GameOfLife game = new GameOfLife(grid);
        game.nextGeneration();
        return game.getGrid();
    }
}
