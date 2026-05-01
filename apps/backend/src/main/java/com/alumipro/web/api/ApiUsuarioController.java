/* archivo que maneja usuarios */
package com.alumipro.web.api;

import com.alumipro.domain.entity.RolUsuario;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class ApiUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiUsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static class UsuarioView {
        public Integer id;
        public String nombre;
        public String correo;
        public String rol;

        public UsuarioView(Integer id, String nombre, String correo, String rol) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
        }
    }

    public static class UsuarioUpsert {
        public String nombre;
        public String correo;
        public String password;
        public String rol;
    }

    @GetMapping
    public List<UsuarioView> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioView(u.getId(), u.getNombre(), u.getCorreo(), u.getRol().name()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioView crear(@RequestBody UsuarioUpsert req) {
        String correo = req.correo == null ? "" : req.correo.trim();
        String nombre = req.nombre == null ? "" : req.nombre.trim();
        String password = req.password == null ? "" : req.password;

        if (nombre.isEmpty()) throw new IllegalArgumentException("Nombre es obligatorio.");
        if (correo.isEmpty()) throw new IllegalArgumentException("Correo es obligatorio.");
        if (password.isEmpty()) throw new IllegalArgumentException("Contraseña es obligatoria.");
        if (usuarioRepository.existsByCorreo(correo)) throw new IllegalArgumentException("Correo ya registrado.");

        RolUsuario rol = RolUsuario.VENDEDOR;
        if (req.rol != null && !req.rol.trim().isEmpty()) {
            rol = RolUsuario.valueOf(req.rol.trim().toUpperCase());
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setPassword(passwordEncoder.encode(password));
        u.setRol(rol);

        Usuario saved = usuarioRepository.save(u);
        return new UsuarioView(saved.getId(), saved.getNombre(), saved.getCorreo(), saved.getRol().name());
    }

    @PutMapping("/{id}")
    public UsuarioView actualizar(@PathVariable Integer id, @RequestBody UsuarioUpsert req) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        if (req.nombre != null && !req.nombre.trim().isEmpty()) u.setNombre(req.nombre.trim());

        if (req.correo != null && !req.correo.trim().isEmpty() && !req.correo.trim().equals(u.getCorreo())) {
            String correo = req.correo.trim();
            if (usuarioRepository.existsByCorreo(correo)) throw new IllegalArgumentException("Correo ya registrado.");
            u.setCorreo(correo);
        }

        if (req.rol != null && !req.rol.trim().isEmpty()) {
            u.setRol(RolUsuario.valueOf(req.rol.trim().toUpperCase()));
        }

        if (req.password != null && !req.password.trim().isEmpty()) {
            u.setPassword(passwordEncoder.encode(req.password));
        }

        Usuario saved = usuarioRepository.save(u);
        return new UsuarioView(saved.getId(), saved.getNombre(), saved.getCorreo(), saved.getRol().name());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) throw new IllegalArgumentException("Usuario no encontrado.");
        usuarioRepository.deleteById(id);
    }
}
