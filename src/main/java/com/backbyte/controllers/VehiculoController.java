package com.backbyte.controllers;

import com.backbyte.models.Ciudad;
import com.backbyte.models.Coche;
import com.backbyte.models.Moto;
import com.backbyte.models.Vehiculo;
import com.backbyte.models.TipoVehiculo;
import com.backbyte.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping("/vehiculos")
    public String getVehiculos(@RequestParam(required = false) String ciudad,
                               @RequestParam(required = false) String tipo,
                               @RequestParam(required = false) Double minPrecio,
                               @RequestParam(required = false) Double maxPrecio,
                               Model model) {
        model.addAttribute("vehiculos", vehiculoService.getVehiculos(ciudad, tipo, minPrecio, maxPrecio));
        model.addAttribute("ciudades", vehiculoService.getCiudadesDisponibles());
        model.addAttribute("tiposVehiculo", vehiculoService.getTiposVehiculoDisponibles());
        return "vehiculos";
    }

    @GetMapping("/user/vehiculos")
    public String getUserVehiculos(@RequestParam(required = false) String ciudad,
                                   @RequestParam(required = false) String tipo,
                                   @RequestParam(required = false) Double minPrecio,
                                   @RequestParam(required = false) Double maxPrecio,
                                   Model model) {
        model.addAttribute("vehiculos", vehiculoService.getVehiculos(ciudad, tipo, minPrecio, maxPrecio));
        model.addAttribute("ciudades", vehiculoService.getCiudadesDisponibles());
        model.addAttribute("tiposVehiculo", vehiculoService.getTiposVehiculoDisponibles());
        return "/user/userVehiculos"; // Devuelve la plantilla Thymeleaf "userVehiculos.html"
    }


    @GetMapping("/admin/vehiculos")
    public String getAdminVehiculos(@RequestParam(required = false) String ciudad,
                                    @RequestParam(required = false) String tipo,
                                    @RequestParam(required = false) Double minPrecio,
                                    @RequestParam(required = false) Double maxPrecio,
                                    Model model) {
        model.addAttribute("vehiculos", vehiculoService.getVehiculos(ciudad, tipo, minPrecio, maxPrecio));
        model.addAttribute("ciudades", vehiculoService.getCiudadesDisponibles());
        model.addAttribute("tiposVehiculo", vehiculoService.getTiposVehiculoDisponibles());
        return "/admin/adminVehiculos"; // Devuelve la plantilla Thymeleaf "userVehiculos.html"
    }


    // Ruta para añadir un nuevo vehículo (POST /vehiculos/agregar)
    @PostMapping("/vehiculos/agregar")
    public String addVehiculo(@RequestParam String marca,
                              @RequestParam String modelo,
                              @RequestParam String matricula,
                              @RequestParam Double precioDia,
                              @RequestParam Ciudad localizacion,
                              @RequestParam TipoVehiculo tipoVehiculo,
                              @RequestParam(required = false) Integer puertas,
                              @RequestParam(required = false) String color,
                              @RequestParam(required = false) Integer cc) {
        Vehiculo nuevoVehiculo;

        if (tipoVehiculo == TipoVehiculo.Coche){
            Coche coche = new Coche();
            coche.setPuertas(puertas);
            coche.setColor(color);
            nuevoVehiculo = coche;
        } else if (tipoVehiculo == TipoVehiculo.Moto) {
            Moto moto = new Moto();
            moto.setCc(cc);
            nuevoVehiculo = moto;
        } else {
            throw new RuntimeException("Tipo de vehículo no válido");
        }

        // Asignar atributos comunes
        nuevoVehiculo.setMarca(marca);
        nuevoVehiculo.setModelo(modelo);
        nuevoVehiculo.setMatricula(matricula);
        nuevoVehiculo.setPrecioDia(precioDia);
        nuevoVehiculo.setLocalizacion(localizacion);
        nuevoVehiculo.setTipoVehiculo(tipoVehiculo);

        vehiculoService.addVehiculo(nuevoVehiculo);
        return "redirect:/admin/vehiculos";
    }

    // Ruta para editar un vehículo existente (POST /vehiculos/editar/{id})
    @PostMapping("/vehiculos/editar/{id}")
    public String editVehiculo(@PathVariable Integer id,
                               @RequestParam String marca,
                               @RequestParam String modelo,
                               @RequestParam String matricula,
                               @RequestParam Double precioDia,
                               @RequestParam Ciudad localizacion,
                               @RequestParam TipoVehiculo tipoVehiculo,
                               @RequestParam(required = false) Integer puertas,
                               @RequestParam(required = false) String color,
                               @RequestParam(required = false) Integer cc) {
        Vehiculo vehiculoExistente = vehiculoService.getVehiculoById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        // Asignar atributos comunes
        vehiculoExistente.setMarca(marca);
        vehiculoExistente.setModelo(modelo);
        vehiculoExistente.setMatricula(matricula);
        vehiculoExistente.setPrecioDia(precioDia);
        vehiculoExistente.setLocalizacion(localizacion);
        vehiculoExistente.setTipoVehiculo(tipoVehiculo);

        // Actualizar atributos específicos dependiendo del tipo de vehículo
        if (vehiculoExistente instanceof Coche && tipoVehiculo == TipoVehiculo.Coche) {
            Coche coche = (Coche) vehiculoExistente;
            coche.setPuertas(puertas);
            coche.setColor(color);
        } else if (vehiculoExistente instanceof Moto && tipoVehiculo == TipoVehiculo.Moto) {
            Moto moto = (Moto) vehiculoExistente;
            moto.setCc(cc);
        }

        vehiculoService.updateVehiculo(id, vehiculoExistente);
        return "redirect:/admin/vehiculos";
    }


    // Ruta para eliminar un vehículo (GET /vehiculos/eliminar/{id})
    @GetMapping("/vehiculos/eliminar/{id}")
    public String deleteVehiculo(@PathVariable Integer id) {
        vehiculoService.deleteVehiculo(id);
        return "redirect:/admin/vehiculos";
    }
}