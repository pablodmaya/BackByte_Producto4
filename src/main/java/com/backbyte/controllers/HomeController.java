package com.backbyte.controllers;

import com.backbyte.models.Ciudad;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    // Ruta mapeada a la raíz "/"
    @GetMapping("/home")
    public String home(Model model) {
        // Aquí puedes agregar atributos al modelo si es necesario.
        model.addAttribute("ciudades", Ciudad.values()); // Pasamos las opciones del enum al modelo
        return "home";  // Esta es la vista home.html
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        model.addAttribute("ciudades", Ciudad.values()); // Pasamos las opciones del enum al modelo
        return "admin/adminHome";  // Redirige a la página de inicio del admin
    }

    @GetMapping("/user/home")
    public String userHome(Model model) {
        model.addAttribute("ciudades", Ciudad.values()); // Pasamos las opciones del enum al modelo
        return "user/userHome";  // Redirige a la página de inicio del usuario
    }

}