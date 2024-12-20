package com.inventario.security.services;

import com.inventario.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerDetails implements UserDetails {


    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

    public CustomerDetails(Usuario usuario) {
        this.authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_"+rol.getNombre()))
                .collect(Collectors.toSet());
        this.password = usuario.getPassword();
        this.username =  usuario.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
