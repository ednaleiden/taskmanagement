package com.inventario.security.controller;


import com.inventario.entity.Rol;
import com.inventario.entity.Usuario;
import com.inventario.enums.RolEnum;
import com.inventario.security.dto.AuthRequest;
import com.inventario.security.dto.AuthResponse;
import com.inventario.security.dto.RegistroRequest;
import com.inventario.security.repository.RolRepository;
import com.inventario.security.services.JwtUtil;
import com.inventario.security.services.UserDetailsServicesImpl;
import com.inventario.security.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserDetailsServicesImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody AuthRequest authRequest){
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        //Verificar si el usuario existe y su contraseña correcta
        if(userDetails != null && passwordEncoder.matches(authRequest.getPassword(),userDetails.getPassword())){
            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(jwt));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorecto");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registarUsuario(@RequestBody RegistroRequest registroRequest) {
        if (usuarioService.buscarPorNombre(registroRequest.getUsername()) != null) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));

        Set<Rol> roles = new HashSet<>();

        if (registroRequest.getRoles() != null) {
            for (RolEnum rolEnum : registroRequest.getRoles()) {
                Rol rolObj = rolRepository.findByNombre(rolEnum.name());
                if (rolObj != null) {
                    roles.add(rolObj);
                }
            }
            usuario.setRoles(roles);
        }

        // Si no se proporcionan roles válidos, asignar el rol por defecto de usuario
        if (roles.isEmpty()) {
            Rol userRole = rolRepository.findByNombre(RolEnum.ROLE_USER.getRole());
            roles.add(userRole);
            usuario.setRoles(roles);
        }


        usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok().body("{\"message\": \"Usuario registrado exitosamente\"}");
    }

}


/*
{
    "username": "nombre_de_usuario",
    "password": "contraseña",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
*/