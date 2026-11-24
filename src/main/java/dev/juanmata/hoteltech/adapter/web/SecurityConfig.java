package dev.juanmata.hoteltech.adapter.web;

import dev.juanmata.hoteltech.core.constant.UserStatus;
import dev.juanmata.hoteltech.core.port.in.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/login", "/error/**").permitAll()
                        .requestMatchers("/rooms/new", "/rooms/*/status", "/users/**", "/reports/reservations").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/reservations", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserUseCase userService) {
        return email -> userService.findByEmail(email)
                .filter(u -> u.status() == UserStatus.ACTIVE)
                .map(u -> User.withUsername(u.email())
                        .password(u.password())
                        .roles(u.role().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo: " + email));
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}