package gameoflife.controller;

import gameoflife.dto.StartRequest;
import org.springframework.web.bind.annotation.*;
import gameoflife.service.GameService;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:8080",
        "https://game-of-life.castello.ovh"
})
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/preview")
    public boolean[][] preview(@RequestBody StartRequest request) {
        boolean[][] grid = new boolean[20][20];
        return service.applyPattern(grid, request.pattern);
    }

    @PostMapping("/start")
    public boolean[][] start(@RequestBody StartRequest request) {
        return service.start(request.pattern, request.grid);
    }

    @PostMapping("/next")
    public boolean[][] next(@RequestBody boolean[][] grid) {
        return service.next(grid);
    }
}