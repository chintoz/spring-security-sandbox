package es.menasoft.springsecuritysandbox.player.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Player {

    private Integer playerId;
    private String playerName;
}
