package controller;

import model.GameOfLife;
import org.springframework.web.bind.annotation.*;
import patterns.Patterns;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GameController {

    private GameOfLife game;

    @PostMapping("/start")
    public boolean[][] start(@RequestBody String pattern) {

        game = new GameOfLife(new boolean[20][20]);

        switch (pattern) {
            case "blinker":
                Patterns.createBlinker(game.getGrid());
                break;
            case "glider":
                Patterns.createGlider(game.getGrid());
                break;
            case "block":
                Patterns.createBlock(game.getGrid());
                break;
        }

        return game.getGrid();
    }

    @GetMapping("/next")
    public boolean[][] next() {
        game.nextGeneration();
        return game.getGrid();
    }
}