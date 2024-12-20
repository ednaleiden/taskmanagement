package com.inventario.security.dto;

import com.inventario.enums.RolEnum;
import lombok.Data;

import java.util.Set;

@Data
public class RegistroRequest {

    private String username;
    private String password;
    private Set<RolEnum> roles;
}
