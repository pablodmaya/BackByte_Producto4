package com.backbyte.repository;

import com.backbyte.models.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    // Obtener todas las reservas de un cliente específico
    @Query("SELECT a FROM Alquiler a WHERE a.cliente.id_Cliente = :idCliente")
    List<Alquiler> findByClienteId(@Param("idCliente") Long idCliente);

    // Calcular el precio total por reserva (días reservados * precio por día del vehículo)
    @Query(value = """
    SELECT 
        DATEDIFF(a.fecha_fin, a.fecha_inicio) * v.precio_dia AS precio_total
    FROM 
        alquiler a
    JOIN 
        vehiculo v ON a.id_Vehiculo = v.id_Vehiculo
    WHERE 
        a.id_Cliente = :idCliente
    """, nativeQuery = true)
    List<Double> calcularPrecioTotalPorReserva(@Param("idCliente") Long idCliente);

    // Filtrar reservas por cliente y/o fechas
    @Query("SELECT a FROM Alquiler a WHERE " +
            "(:clienteId IS NULL OR a.cliente.id_Cliente = :clienteId) AND " +
            "(:fechaInicio IS NULL OR a.fecha_Inicio >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR a.fecha_Fin <= :fechaFin)")
    List<Alquiler> findByCriteria(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}
