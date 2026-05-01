/* archivo que inicializa usuarios */
package com.alumipro.config;

import com.alumipro.domain.entity.RolUsuario;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            ensure(repo, encoder, "Administrador ALUMIPRO", "admin@alumipro.com", "admin123", RolUsuario.ADMIN);
            ensure(repo, encoder, "Vendedor ALUMIPRO", "vendedor@alumipro.com", "vendedor123", RolUsuario.VENDEDOR);
        };
    }

    private void ensure(UsuarioRepository repo, PasswordEncoder encoder, String nombre, String correo, String rawPass, RolUsuario rol) {
        repo.findByCorreo(correo).ifPresentOrElse(
                u -> {
                    if (u.getRol() != rol) {
                        u.setRol(rol);
                        repo.save(u);
                    }
                },
                () -> {
                    Usuario u = new Usuario();
                    u.setNombre(nombre);
                    u.setCorreo(correo);
                    u.setPassword(encoder.encode(rawPass));
                    u.setRol(rol);
                    repo.save(u);
                }
        );
    }
}
