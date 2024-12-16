package com.backbyte.controllers.api;


import com.backbyte.models.Cliente;
import com.backbyte.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteApiController {

    @Autowired
    ClienteService clienteService;

    // Endpoint para obtener la lista de clientes
    @GetMapping
    public List<Cliente> obtenerClientes() {
        // Lista simulada de clientes
        return clienteService.getClientes();
    }

}
