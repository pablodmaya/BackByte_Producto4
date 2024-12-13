package com.backbyte.service;

import com.backbyte.models.Cliente;
import com.backbyte.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioService usuarioService; // Servicio de usuarios

    // Obtener todos los clientes
    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    // Obtener un cliente por su ID
    public Optional<Cliente> getClienteById(Integer id) {
        return clienteRepository.findById(Long.valueOf(id));
    }

    // Agregar un nuevo cliente
    public Cliente addCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Actualizar un cliente existente
    public Cliente updateCliente(Integer id, Cliente clienteDetalles) {
        return clienteRepository.findById(Long.valueOf(id))
                .map(cliente -> {
                    cliente.setNombre(clienteDetalles.getNombre());
                    cliente.setApellido(clienteDetalles.getApellido());
                    cliente.setDni(clienteDetalles.getDni());
                    cliente.setDireccion(clienteDetalles.getDireccion());
                    cliente.setTelefono(clienteDetalles.getTelefono());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    // Eliminar un cliente junto con su usuario asociado
    public void deleteCliente(Integer id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(Long.valueOf(id));
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            // Eliminar el cliente primero
            clienteRepository.delete(cliente);

            // Si el cliente tiene un usuario asociado, eliminarlo
            if (cliente.getUsuario() != null) {
                usuarioService.deleteUsuario(cliente.getUsuario().getId_Usuario());
            }
        } else {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
    }
}
