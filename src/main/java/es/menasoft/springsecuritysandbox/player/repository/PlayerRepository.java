package es.menasoft.springsecuritysandbox.player.repository;

import es.menasoft.springsecuritysandbox.player.model.Player;
import es.menasoft.springsecuritysandbox.player.service.PlayerNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerRepository {

    private static Map<Integer, Player> playersMap = new ConcurrentHashMap<>();

    public PlayerRepository() {
        playersMap.putAll(Map.of(1, Player.builder().playerId(1).playerName("Arnold Woods").build(),
                2, Player.builder().playerId(2).playerName("Tiger Cantlay").build(),
                3, Player.builder().playerId(3).playerName("Bryson Koepka").build()));
    }

    public List<Player> findAll() {
        return playersMap.values().stream().toList();
    }

    public Optional<Player> findById(Integer playerId) {
        return Optional.ofNullable(playersMap.get(playerId));
    }

    public Player save(Player player) {
        Integer playerId = playersMap.keySet().stream().max(Integer::compare).orElse(0) + 1;
        player.setPlayerId(playerId);
        playersMap.put(playerId, player);
        return player;
    }

    public void delete(Integer playerId) {
        playersMap.remove(playerId);
    }

    public void updatePlayer(Player player) {
        if (playersMap.get(player.getPlayerId()) == null) {
            throw new PlayerNotFoundException();
        }
        playersMap.put(player.getPlayerId(), player);
    }
}
