package es.menasoft.springsecuritysandbox.player.service;

import es.menasoft.springsecuritysandbox.player.model.Player;
import es.menasoft.springsecuritysandbox.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findById(Integer playerId) {
        return playerRepository.findById(playerId).orElseThrow(PlayerNotFoundException::new);
    }
}
