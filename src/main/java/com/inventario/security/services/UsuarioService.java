package com.inventario.security.services;

import com.inventario.entity.Rol;
import com.inventario.entity.Usuario;
import com.inventario.security.repository.RolRepository;
import com.inventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario buscarPorNombre(String username){
        return usuarioRepository.findByUsername(username);
    }

    public Usuario guardarUsuario(Usuario usuario){
        usuario.setUsername(usuario.getUsername());
        usuario.setRoles(usuario.getRoles());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        for(Rol rol : usuario.getRoles()) {
            rol.getUsuarios().add(usuarioGuardado);
        }
        return usuarioGuardado;
    }
}
