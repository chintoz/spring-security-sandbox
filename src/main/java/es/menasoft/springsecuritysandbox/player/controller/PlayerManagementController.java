package es.menasoft.springsecuritysandbox.player.controller;

import es.menasoft.springsecuritysandbox.player.model.Player;
import es.menasoft.springsecuritysandbox.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("management/api/v1/player")
public class PlayerManagementController {

    private final PlayerRepository playerRepository;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @PostMapping
    public void registerNewPlayer(@RequestBody Player player) {
        playerRepository.save(player);
    }

    @DeleteMapping("/{playerId}")
    public void deletePlayer(@PathVariable("playerId") Integer playerId) {
        playerRepository.delete(playerId);
    }

    @PutMapping("/{playerId}")
    public void updatePlayer(@PathVariable("playerId") Integer playerId, @RequestBody Player player) {
        if (playerId == null) {
            throw new IllegalArgumentException("Player Id not specified");
        }
        player.setPlayerId(playerId);
        playerRepository.updatePlayer(player);
    }
}
