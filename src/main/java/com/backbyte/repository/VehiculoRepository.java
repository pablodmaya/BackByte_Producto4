package com.backbyte.repository;

import com.backbyte.models.Vehiculo;
import com.backbyte.models.Ciudad;
import com.backbyte.models.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Consulta para filtrar vehículos por ciudad, tipo y precio entre un rango
    List<Vehiculo> findByLocalizacionAndTipoVehiculoAndPrecioDiaBetween(Ciudad localizacion, TipoVehiculo tipoVehiculo, Double minPrecio, Double maxPrecio);

    // Consulta para filtrar vehículos por ciudad y tipo
    List<Vehiculo> findByLocalizacionAndTipoVehiculo(Ciudad localizacion, TipoVehiculo tipoVehiculo);

    // Consulta para filtrar vehículos por solo precio entre un rango
    List<Vehiculo> findByPrecioDiaBetween(Double minPrecio, Double maxPrecio);

    // Consulta para filtrar vehículos solo por ciudad
    List<Vehiculo> findByLocalizacion(Ciudad localizacion);

    // Consulta para filtrar vehículos solo por tipo
    List<Vehiculo> findByTipoVehiculo(TipoVehiculo tipo);

    // Consulta para filtrar vehículos por precio mínimo
    List<Vehiculo> findByPrecioDiaGreaterThanEqual(Double precio);

    // Consulta para filtrar vehículos por precio máximo
    List<Vehiculo> findByPrecioDiaLessThanEqual(Double precio);

    @Query("SELECT v FROM Vehiculo v WHERE v.tipoVehiculo = :tipoVehiculo " +
            "AND v.localizacion = :ciudadRecogida " +
            "AND v.id_Vehiculo NOT IN (" +  // Cambié 'idVehiculo' por 'id_Vehiculo'
            "    SELECT a.vehiculo.id_Vehiculo FROM Alquiler a " +  // Cambié 'idVehiculo' por 'id_Vehiculo'
            "    WHERE (a.fecha_Inicio <= :fecha_Fin AND a.fecha_Fin >= :fecha_Inicio)" +
            ")")
    List<Vehiculo> findAvailableVehicles(@Param("tipoVehiculo") TipoVehiculo tipoVehiculo,
                                         @Param("ciudadRecogida") Ciudad ciudadRecogida,
                                         @Param("fecha_Inicio") java.sql.Date fechaInicio,
                                         @Param("fecha_Fin") java.sql.Date fechaFin);
}