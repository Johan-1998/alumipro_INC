/* archivo de clase authuserdetailsservice */
package com.alumipro.security;

import com.alumipro.domain.entity.Usuario;
import com.alumipro.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UserPrincipal(u.getId(), u.getCorreo(), u.getPassword(), u.getRol());
    }
}
