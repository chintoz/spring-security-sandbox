package es.menasoft.springsecuritysandbox.player.controller;

import es.menasoft.springsecuritysandbox.player.model.Player;
import es.menasoft.springsecuritysandbox.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping(path = "{playerId}")
    public Player getPlayer(@PathVariable("playerId") Integer playerId) {
        return playerService.findById(playerId);
    }
}
