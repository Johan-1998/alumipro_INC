/* archivo que maneja el acceso */
package com.alumipro.web.api;

import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import com.alumipro.security.JwtService;
import com.alumipro.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public ApiAuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public static class LoginRequest {
        public String correo;
        public String password;
    }

    public static class UserView {
        public Integer id;
        public String nombre;
        public String correo;
        public String rol;

        public UserView(Integer id, String nombre, String correo, String rol) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
        }
    }

    public static class LoginResponse {
        public String token;
        public UserView user;

        public LoginResponse(String token, UserView user) {
            this.token = token;
            this.user = user;
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest req) {
        String correo = req.correo == null ? "" : req.correo.trim();
        String password = req.password == null ? "" : req.password;
        if (correo.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Correo y contraseña son obligatorios.");
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, password)
        );

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Usuario u = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        String token = jwtService.createToken(u.getCorreo(), u.getRol().name(), u.getId());
        return new LoginResponse(token, new UserView(u.getId(), u.getNombre(), u.getCorreo(), u.getRol().name()));
    }

    @GetMapping("/me")
    public UserView me(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new IllegalArgumentException("No autenticado.");
        }
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return new UserView(u.getId(), u.getNombre(), u.getCorreo(), u.getRol().name());
    }
}
