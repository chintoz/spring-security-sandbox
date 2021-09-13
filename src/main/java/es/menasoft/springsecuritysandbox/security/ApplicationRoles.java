package es.menasoft.springsecuritysandbox.security;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static es.menasoft.springsecuritysandbox.security.ApplicationPermissions.PLAYER_READ;
import static es.menasoft.springsecuritysandbox.security.ApplicationPermissions.TOURNAMENT_READ;

@RequiredArgsConstructor
public enum ApplicationRoles {
    PLAYER(Set.of(PLAYER_READ)),
    ADMIN(new HashSet<>(Arrays.stream(ApplicationPermissions.values()).toList())),
    COMMITTEE(Set.of(TOURNAMENT_READ));

    private final Set<ApplicationPermissions> permissions;
}
