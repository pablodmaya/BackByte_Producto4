package com.backbyte.controllers.api;


import com.backbyte.models.Alquiler;
import com.backbyte.service.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alquilerVehiculos")
public class VehiculoApiController {

    @Autowired
    AlquilerService alquilerService;

    @GetMapping
    public List<Alquiler> obtenerAlquileres() {
        return alquilerService.getAlquiler();

    }
}
