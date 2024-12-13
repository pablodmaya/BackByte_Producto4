package com.backbyte.controllers;

import com.backbyte.models.Cliente;
import com.backbyte.service.ClienteService;
import com.backbyte.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/admin/clientes")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.getClientes();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @PostMapping("/clientes/agregar")
    public String addCliente(@RequestParam String nombre,
                             @RequestParam String apellido,
                             @RequestParam String dni,
                             @RequestParam String direccion,
                             @RequestParam String telefono) {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setDni(dni);
        nuevoCliente.setDireccion(direccion);
        nuevoCliente.setTelefono(telefono);

        clienteService.addCliente(nuevoCliente);
        return "redirect:/admin/clientes";
    }

    @PostMapping("/clientes/editar/{id}")
    public String editCliente(@PathVariable Integer id,
                              @RequestParam String nombre,
                              @RequestParam String apellido,
                              @RequestParam String dni,
                              @RequestParam String direccion,
                              @RequestParam String telefono) {
        Cliente clienteExistente = clienteService.getClienteById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        clienteExistente.setNombre(nombre);
        clienteExistente.setApellido(apellido);
        clienteExistente.setDni(dni);
        clienteExistente.setDireccion(direccion);
        clienteExistente.setTelefono(telefono);

        clienteService.updateCliente(id, clienteExistente);
        return "redirect:/admin/clientes";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String deleteCliente(@PathVariable Integer id) {
        Cliente cliente = clienteService.getClienteById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        // Eliminar el usuario asociado al cliente
        if (cliente.getUsuario() != null) {
            usuarioService.deleteUsuario(cliente.getUsuario().getId_Usuario());
        }

        // Eliminar el cliente
        clienteService.deleteCliente(id);

        return "redirect:/admin/clientes";
    }
}
