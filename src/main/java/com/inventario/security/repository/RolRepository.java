package com.inventario.security.repository;

import com.inventario.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {

        Rol findRolByNombre(String nombre);

        boolean existsByNombre(String nombre);

}
