/* archivo de configuración security */
package com.alumipro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/usuarios/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/productos/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/clientes/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/clientes/**").hasAnyRole("ADMIN", "VENDEDOR")
                .antMatchers(HttpMethod.PUT, "/api/clientes/**").hasAnyRole("ADMIN", "VENDEDOR")
                .antMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/ventas/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyRole("ADMIN", "VENDEDOR")

                // FIX: /api/movil/** no estaba en las reglas → caía en .anyRequest().authenticated()
                // pero como no tenía regla explícita, Spring lo bloqueaba con 403 en algunos casos.
                // Se deja authenticated() para que el JWT siga siendo requerido.
                .antMatchers(HttpMethod.GET, "/api/movil/**").authenticated()

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
