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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

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
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Enables cookie for token to prevent CSRF
                //.and()
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
                .formLogin() // Once it's authenticated, information is stored in cookie session id. Default cookie expiration by default 30 minutes
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/players", true)
                .and().rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))   // Default 2 weeks (overridden to 21)
                .key("somethingverysecure") // A key to be used to hash username:expiration for the remember-me cookie
                .and().logout().logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))// With CSRF this operation should be a POST
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
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
