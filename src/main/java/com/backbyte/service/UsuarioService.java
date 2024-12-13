package com.backbyte.service;

import com.backbyte.models.Usuario;
import com.backbyte.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método para cargar el usuario por nombre de usuario (Spring Security)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getNombreUsuario(),
                usuario.getPasswordEncriptada(), // Contraseña encriptada
                usuario.getAuthorities()
        );
    }

    // Obtener un usuario por su ID
    public Optional<Usuario> getUsuarioById(Integer id) {
        return usuarioRepository.findById(Math.toIntExact(Long.valueOf(id)));
    }

    // Eliminar un usuario por su ID
    public void deleteUsuario(Integer id) {
        if (usuarioRepository.existsById(Math.toIntExact(Long.valueOf(id)))) {
            usuarioRepository.deleteById(Math.toIntExact(Long.valueOf(id)));
        } else {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
    }
}
