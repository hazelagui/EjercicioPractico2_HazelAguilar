package com.plataforma.academica.controllers;

import com.plataforma.academica.domain.Usuario;
import com.plataforma.academica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public EstudianteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("usuario", usuario);
        return "estudiante/perfil";
    }
}
