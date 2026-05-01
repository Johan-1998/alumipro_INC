/* archivo de clase userprincipal */
package com.alumipro.security;

import com.alumipro.domain.entity.RolUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Integer id;
    private final String correo;
    private final String password;
    private final RolUsuario rol;

    public UserPrincipal(Integer id, String correo, String password, RolUsuario rol) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public Integer getId() { return id; }
    public RolUsuario getRol() { return rol; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return correo; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
