package com.alumipro.web.api;

import com.alumipro.domain.entity.RolUsuario;
import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import com.alumipro.security.JwtService;
import com.alumipro.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiAuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ApiAuthController controller;

    @Test
    void loginDebeRetornarTokenYUsuario() {
        ApiAuthController.LoginRequest request = new ApiAuthController.LoginRequest();
        request.correo = "admin@alumipro.com";
        request.password = "admin123";

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Administrador ALUMIPRO");
        usuario.setCorreo("admin@alumipro.com");
        usuario.setRol(RolUsuario.ADMIN);
        usuario.setPassword("hash");

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new UserPrincipal(usuario.getId(), usuario.getCorreo(), usuario.getPassword(), usuario.getRol()),
                null,
                new UserPrincipal(usuario.getId(), usuario.getCorreo(), usuario.getPassword(), usuario.getRol()).getAuthorities()
        );

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(usuarioRepository.findByCorreo("admin@alumipro.com")).thenReturn(Optional.of(usuario));
        when(jwtService.createToken("admin@alumipro.com", "ADMIN", 1)).thenReturn("jwt-demo");

        ApiAuthController.LoginResponse response = controller.login(request);

        assertEquals("jwt-demo", response.token);
        assertEquals("Administrador ALUMIPRO", response.user.nombre);
        assertEquals("ADMIN", response.user.rol);
        verify(authenticationManager).authenticate(any(Authentication.class));
    }

    @Test
    void loginDebeValidarCamposObligatorios() {
        ApiAuthController.LoginRequest request = new ApiAuthController.LoginRequest();
        request.correo = "";
        request.password = "";

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.login(request));

        assertEquals("Correo y contraseña son obligatorios.", ex.getMessage());
        verify(authenticationManager, never()).authenticate(any(Authentication.class));
    }
}
