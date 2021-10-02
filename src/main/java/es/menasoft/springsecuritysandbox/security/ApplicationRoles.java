package es.menasoft.springsecuritysandbox.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static es.menasoft.springsecuritysandbox.security.ApplicationPermissions.PLAYER_READ;
import static es.menasoft.springsecuritysandbox.security.ApplicationPermissions.TOURNAMENT_READ;

@RequiredArgsConstructor
public enum ApplicationRoles {
    PLAYER(Set.of(PLAYER_READ)),
    ADMIN(new HashSet<>(Arrays.stream(ApplicationPermissions.values()).toList())),
    COMMITTEE(Set.of(TOURNAMENT_READ));

    private final Set<ApplicationPermissions> permissions;

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> permissionsSet = this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toSet());
        permissionsSet.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissionsSet;
    }
}
