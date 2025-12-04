package com.plataforma.academica.controllers;

import com.plataforma.academica.service.RolService;
import com.plataforma.academica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UsuarioService usuarioService;
    private final RolService rolService;
    
    @Autowired
    public AdminController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalRoles", rolService.listarTodos().size());
        model.addAttribute("usuariosActivos", usuarioService.contarPorEstado().get("activos"));
        model.addAttribute("usuariosInactivos", usuarioService.contarPorEstado().get("inactivos"));
        model.addAttribute("usuariosPorRol", usuarioService.contarPorRol());
        model.addAttribute("usuariosRecientes", usuarioService.listarUsuariosRecientes(7));
        return "admin/dashboard";
    }
}
