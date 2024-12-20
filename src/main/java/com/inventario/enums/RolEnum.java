package com.inventario.enums;

public enum RolEnum {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String role;

    RolEnum(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
