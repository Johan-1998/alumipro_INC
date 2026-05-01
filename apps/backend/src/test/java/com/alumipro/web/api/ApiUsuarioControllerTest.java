package com.alumipro.web.api;

import com.alumipro.domain.entity.RolUsuario;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ApiUsuarioController controller;

    @Test
    void crearDebeGuardarUsuarioConPasswordEncriptado() {
        ApiUsuarioController.UsuarioUpsert request = new ApiUsuarioController.UsuarioUpsert();
        request.nombre = "Nuevo admin";
        request.correo = "nuevo@alumipro.com";
        request.password = "secreto123";
        request.rol = "ADMIN";

        when(usuarioRepository.existsByCorreo("nuevo@alumipro.com")).thenReturn(false);
        when(passwordEncoder.encode("secreto123")).thenReturn("hash-123");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario usuario = inv.getArgument(0);
            usuario.setId(10);
            return usuario;
        });

        ApiUsuarioController.UsuarioView response = controller.crear(request);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario saved = captor.getValue();
        assertEquals("hash-123", saved.getPassword());
        assertEquals(RolUsuario.ADMIN, saved.getRol());
        assertEquals(10, response.id);
        assertEquals("ADMIN", response.rol);
    }

    @Test
    void crearDebeRechazarCorreoDuplicado() {
        ApiUsuarioController.UsuarioUpsert request = new ApiUsuarioController.UsuarioUpsert();
        request.nombre = "Duplicado";
        request.correo = "admin@alumipro.com";
        request.password = "123456";

        when(usuarioRepository.existsByCorreo("admin@alumipro.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.crear(request));

        assertEquals("Correo ya registrado.", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarDebeCambiarRolYPasswordSiSeInforman() {
        Usuario usuario = new Usuario();
        usuario.setId(5);
        usuario.setNombre("Vendedor base");
        usuario.setCorreo("vendedor@alumipro.com");
        usuario.setPassword("hash-viejo");
        usuario.setRol(RolUsuario.VENDEDOR);

        ApiUsuarioController.UsuarioUpsert request = new ApiUsuarioController.UsuarioUpsert();
        request.nombre = "Vendedor actualizado";
        request.password = "nuevaClave";
        request.rol = "ADMIN";

        when(usuarioRepository.findById(5)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nuevaClave")).thenReturn("hash-nuevo");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        ApiUsuarioController.UsuarioView response = controller.actualizar(5, request);

        assertEquals("Vendedor actualizado", response.nombre);
        assertEquals("ADMIN", response.rol);
        assertEquals("hash-nuevo", usuario.getPassword());
    }
}
