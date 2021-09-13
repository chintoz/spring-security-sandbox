package es.menasoft.springsecuritysandbox.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApplicationPermissions {
    PLAYER_READ("player:read"),
    PLAYER_WRITE("player:write"),
    TOURNAMENT_READ("tournament:read"),
    TOURNAMENT_WRITE("tournament:write");

    private final String permissionName;
}
