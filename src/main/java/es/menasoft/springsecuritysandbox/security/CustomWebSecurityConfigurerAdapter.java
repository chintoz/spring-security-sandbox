package es.menasoft.springsecuritysandbox.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static es.menasoft.springsecuritysandbox.security.ApplicationPermissions.PLAYER_WRITE;
import static es.menasoft.springsecuritysandbox.security.ApplicationRoles.ADMIN;
import static es.menasoft.springsecuritysandbox.security.ApplicationRoles.PLAYER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // To be able to use annotations for authorization such as @PreAuthorize
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/v1/player/**").hasRole(PLAYER.name())

                // These matchers could be replaced by @PreAuthorize annotation
                //.antMatchers(DELETE, "/management/api/**/player/**").hasAuthority(PLAYER_WRITE.getPermissionName())
                //.antMatchers(POST, "/management/api/**/player/**").hasAuthority(PLAYER_WRITE.getPermissionName())
                //.antMatchers(PUT, "/management/api/**/player/**").hasAuthority(PLAYER_WRITE.getPermissionName())
                //.antMatchers(GET, "/management/api/**/player/**").hasAnyRole(ADMIN.name())

                // Add antMatchers to check operations using permissions.
                // Create a management API for players and tournaments.
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(buildPlayer("awoods", "awoods00"),
                buildPlayer("tcantlay", "tcantlay00"),
                buildPlayer("bkoepka", "bkoepka00"),
                buildAdmin("admin", "admin00"));
    }

    private UserDetails buildAdmin(String username, String password) {
        return buildUser(username, password, ADMIN);
    }

    private UserDetails buildPlayer(String username, String password) {
        return buildUser(username, password, PLAYER);
    }

    private UserDetails buildUser(String username, String password, ApplicationRoles role) {
        return User.builder()
                .username(username)
                .password(passwordEncoder().encode(password))
                //.roles(role.name())
                .authorities(role.getGrantedAuthorities())
                .build();
    }
}
