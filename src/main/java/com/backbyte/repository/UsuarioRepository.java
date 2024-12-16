package com.backbyte.repository;

import com.backbyte.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Metodo para buscar usuarios por nombre
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Metodo para buscar usuarios por email
    Optional<Usuario> findByEmail(String email);

    // Metodo para verificar si existe un usuario por nombre
    boolean existsByNombreUsuario(String nombreUsuario);

    // Metodo para verificar si existe un usuario por email
    boolean existsByEmail(String email);

    //Optional<Usuario> findById_Usuario(Integer idUsuario);
}