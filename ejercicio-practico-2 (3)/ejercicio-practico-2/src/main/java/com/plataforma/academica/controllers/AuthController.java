package com.plataforma.academica.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas. Por favor, intente de nuevo.");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Ha cerrado sesión exitosamente.");
        }
        return "auth/login";
    }
    
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "auth/acceso-denegado";
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
