package gameoflife.controller;

import gameoflife.dto.GameState;
import gameoflife.dto.NextRequest;
import gameoflife.dto.PreviewRequest;
import gameoflife.service.GameService;
import org.springframework.web.bind.annotation.*;

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

    // Charge un pattern prédéfini : renvoie la grille dimensionnée + le mode tore
    @PostMapping("/preview")
    public GameState preview(@RequestBody PreviewRequest request) {
        return service.preview(request.pattern);
    }

    // Calcule la génération suivante à partir de la grille et du mode tore reçus
    @PostMapping("/next")
    public boolean[][] next(@RequestBody NextRequest request) {
        return service.next(request.grid, request.torus);
    }
}
