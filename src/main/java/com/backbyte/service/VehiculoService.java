package com.backbyte.service;

import com.backbyte.models.Ciudad;
import com.backbyte.models.TipoVehiculo;
import com.backbyte.models.Vehiculo;
import com.backbyte.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // Metodo para obtener los vehículos con filtros dinámicos
    public List<Vehiculo> getVehiculos(String ciudad, String tipo, Double minPrecio, Double maxPrecio) {
        // Si no hay filtros, devolver todos los vehículos
        if (ciudad == null && tipo == null && minPrecio == null && maxPrecio == null) {
            return vehiculoRepository.findAll();
        }

        // Convertir la ciudad y el tipo de vehículo si no son nulos
        Ciudad ciudadEnum = null;
        TipoVehiculo tipoVehiculoEnum = null;

        if (ciudad != null && !ciudad.isEmpty()) {
            try {
                ciudadEnum = Ciudad.valueOf(ciudad);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de ciudad no válido: " + ciudad, e);
            }
        }

        if (tipo != null && !tipo.isEmpty()) {
            try {
                tipoVehiculoEnum = TipoVehiculo.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de tipo de vehículo no válido: " + tipo, e);
            }
        }

        // Aplicar lógica de filtros
        if (ciudadEnum != null && tipoVehiculoEnum != null && minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculoAndPrecioDiaBetween(ciudadEnum, tipoVehiculoEnum, minPrecio, maxPrecio);
        }

        if (ciudadEnum != null && tipoVehiculoEnum != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculo(ciudadEnum, tipoVehiculoEnum);
        }

        if (minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaBetween(minPrecio, maxPrecio);
        }

        if (ciudadEnum != null) {
            return vehiculoRepository.findByLocalizacion(ciudadEnum);
        }

        if (tipoVehiculoEnum != null) {
            return vehiculoRepository.findByTipoVehiculo(tipoVehiculoEnum);
        }

        if (minPrecio != null) {
            return vehiculoRepository.findByPrecioDiaGreaterThanEqual(minPrecio);
        }

        if (maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaLessThanEqual(maxPrecio);
        }

        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> getUserVehiculos(String ciudad, String tipo, Double minPrecio, Double maxPrecio) {
        // Si no hay filtros, devolver todos los vehículos
        if (ciudad == null && tipo == null && minPrecio == null && maxPrecio == null) {
            return vehiculoRepository.findAll();
        }

        // Convertir la ciudad y el tipo de vehículo si no son nulos
        Ciudad ciudadEnum = null;
        TipoVehiculo tipoVehiculoEnum = null;

        if (ciudad != null && !ciudad.isEmpty()) {
            try {
                ciudadEnum = Ciudad.valueOf(ciudad);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de ciudad no válido: " + ciudad, e);
            }
        }

        if (tipo != null && !tipo.isEmpty()) {
            try {
                tipoVehiculoEnum = TipoVehiculo.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de tipo de vehículo no válido: " + tipo, e);
            }
        }

        // Aplicar lógica de filtros
        if (ciudadEnum != null && tipoVehiculoEnum != null && minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculoAndPrecioDiaBetween(ciudadEnum, tipoVehiculoEnum, minPrecio, maxPrecio);
        }

        if (ciudadEnum != null && tipoVehiculoEnum != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculo(ciudadEnum, tipoVehiculoEnum);
        }

        if (minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaBetween(minPrecio, maxPrecio);
        }

        if (ciudadEnum != null) {
            return vehiculoRepository.findByLocalizacion(ciudadEnum);
        }

        if (tipoVehiculoEnum != null) {
            return vehiculoRepository.findByTipoVehiculo(tipoVehiculoEnum);
        }

        if (minPrecio != null) {
            return vehiculoRepository.findByPrecioDiaGreaterThanEqual(minPrecio);
        }

        if (maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaLessThanEqual(maxPrecio);
        }

        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> getAdminVehiculos(String ciudad, String tipo, Double minPrecio, Double maxPrecio) {
        // Si no hay filtros, devolver todos los vehículos
        if (ciudad == null && tipo == null && minPrecio == null && maxPrecio == null) {
            return vehiculoRepository.findAll();
        }

        // Convertir la ciudad y el tipo de vehículo si no son nulos
        Ciudad ciudadEnum = null;
        TipoVehiculo tipoVehiculoEnum = null;

        if (ciudad != null && !ciudad.isEmpty()) {
            try {
                ciudadEnum = Ciudad.valueOf(ciudad);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de ciudad no válido: " + ciudad, e);
            }
        }

        if (tipo != null && !tipo.isEmpty()) {
            try {
                tipoVehiculoEnum = TipoVehiculo.valueOf(tipo);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Valor de tipo de vehículo no válido: " + tipo, e);
            }
        }

        // Aplicar lógica de filtros
        if (ciudadEnum != null && tipoVehiculoEnum != null && minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculoAndPrecioDiaBetween(ciudadEnum, tipoVehiculoEnum, minPrecio, maxPrecio);
        }

        if (ciudadEnum != null && tipoVehiculoEnum != null) {
            return vehiculoRepository.findByLocalizacionAndTipoVehiculo(ciudadEnum, tipoVehiculoEnum);
        }

        if (minPrecio != null && maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaBetween(minPrecio, maxPrecio);
        }

        if (ciudadEnum != null) {
            return vehiculoRepository.findByLocalizacion(ciudadEnum);
        }

        if (tipoVehiculoEnum != null) {
            return vehiculoRepository.findByTipoVehiculo(tipoVehiculoEnum);
        }

        if (minPrecio != null) {
            return vehiculoRepository.findByPrecioDiaGreaterThanEqual(minPrecio);
        }

        if (maxPrecio != null) {
            return vehiculoRepository.findByPrecioDiaLessThanEqual(maxPrecio);
        }

        return vehiculoRepository.findAll();
    }

    // Metodo para obtener un vehículo por su ID
    public Optional<Vehiculo> getVehiculoById(Integer id) {
        return vehiculoRepository.findById(Long.valueOf(id));
    }

    public Vehiculo addVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    // Método para actualizar un vehículo existente
    public Vehiculo updateVehiculo(Integer id, Vehiculo vehiculoDetalles) {
        return vehiculoRepository.findById(Long.valueOf(id))
                .map(vehiculo -> {
                    vehiculo.setMarca(vehiculoDetalles.getMarca());
                    vehiculo.setModelo(vehiculoDetalles.getModelo());
                    vehiculo.setMatricula(vehiculoDetalles.getMatricula());
                    vehiculo.setPrecioDia(vehiculoDetalles.getPrecioDia());
                    vehiculo.setLocalizacion(vehiculoDetalles.getLocalizacion());
                    vehiculo.setTipoVehiculo(vehiculoDetalles.getTipoVehiculo());
                    return vehiculoRepository.save(vehiculo);
                })
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
    }

    // Método para eliminar un vehículo
    public void deleteVehiculo(Integer id) {
        if (vehiculoRepository.existsById(Long.valueOf(id))) {
            vehiculoRepository.deleteById(Long.valueOf(id));
        } else {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
    }

    // Método para obtener las ciudades disponibles
    public List<Ciudad> getCiudadesDisponibles() {
        return Arrays.asList(Ciudad.values());
    }

    // Método para obtener los tipos de vehículo disponibles
    public List<TipoVehiculo> getTiposVehiculoDisponibles() {
        return Arrays.asList(TipoVehiculo.values());
    }
}
