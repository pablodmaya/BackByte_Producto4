package com.backbyte.controllers;

import com.backbyte.models.Ciudad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";  // Muestra la vista del formulario de login
    }
    @GetMapping("/registro")
    public String registro() {
        return "registro";  // Muestra la vista del formulario de registro
    }
}
