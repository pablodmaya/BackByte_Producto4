package com.backbyte.controllers;

import com.backbyte.models.Cliente;
import com.backbyte.models.Usuario;
import com.backbyte.models.TipoUsuario;
import com.backbyte.service.UsuarioRegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioRegistroController {

    @Autowired
    private UsuarioRegistroService usuarioRegistroService;

    @RequestMapping("/registro/registrarUsuario")
    public String registrarUsuario(
            @RequestParam String nombreUsuario,
            @RequestParam String password,
            @RequestParam String email) {

        // Encriptar la contraseña
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordEncriptada = encoder.encode(password);
        TipoUsuario tipo_Usuario = TipoUsuario.user;
        Boolean es_Cliente = false;
        System.out.println("Datos: " + nombreUsuario + password + passwordEncriptada + email + es_Cliente);
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setPasswordEncriptada(passwordEncriptada);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setTipo_Usuario(tipo_Usuario);
        nuevoUsuario.setEs_Cliente(es_Cliente);

        usuarioRegistroService.registrarUsuario(nuevoUsuario);

        return "redirect:/home";
    }


}
