package gameoflife.service;

import gameoflife.model.GameOfLife;
import org.springframework.stereotype.Service;
import gameoflife.patterns.Patterns;

@Service
public class GameService {

    public boolean[][] applyPattern(boolean[][] grid, String pattern) {

        switch (pattern) {
            case "blinker" -> Patterns.createBlinker(grid);
            case "glider" -> Patterns.createGlider(grid);
            case "block" -> Patterns.createBlock(grid);
        }

        return grid;
    }

    public boolean[][] preview(String pattern) {

        boolean[][] grid = new boolean[20][20];

        applyPattern(grid, pattern);

        return grid;
    }

    public boolean[][] start(String pattern, boolean[][] customGrid) {
        if ("custom".equals(pattern)) {
            // La partie a été dessinée par le joueur : on part de sa grille
            return customGrid;
        }

        // La partie démarre avec un pattern prédéfini
        boolean[][] grid = new boolean[20][20];
        applyPattern(grid, pattern);
        return grid;
    }

    // Calcule la génération suivante à partir de la grille reçue.
    // Aucun état conservé côté serveur : chaque joueur envoie sa propre grille.
    public boolean[][] next(boolean[][] grid) {
        GameOfLife game = new GameOfLife(grid);
        game.nextGeneration();
        return game.getGrid();
    }
}