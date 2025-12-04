package com.plataforma.academica.controllers;

import com.plataforma.academica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profesor")
public class ProfesorController {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ProfesorController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/reportes")
    public String reportes(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("conteoEstado", usuarioService.contarPorEstado());
        model.addAttribute("conteoPorRol", usuarioService.contarPorRol());
        model.addAttribute("usuariosRecientes", usuarioService.listarUsuariosRecientes(30));
        model.addAttribute("estudiantes", usuarioService.buscarPorRol("ESTUDIANTE"));
        return "profesor/reportes";
    }
}
