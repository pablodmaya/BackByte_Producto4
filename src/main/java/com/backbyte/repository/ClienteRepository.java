package com.backbyte.repository;


import com.backbyte.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    Cliente findBydni(String dni);

    @Query("SELECT c FROM Cliente c WHERE c.usuario.id = :idUsuario")
    Cliente findByUsuarioId(@Param("idUsuario") Long idUsuario);

}