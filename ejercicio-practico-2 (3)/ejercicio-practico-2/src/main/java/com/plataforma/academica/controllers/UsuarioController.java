package com.plataforma.academica.controllers;

import com.plataforma.academica.domain.Usuario;
import com.plataforma.academica.service.RolService;
import com.plataforma.academica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final RolService rolService;
    
    @Autowired
    public UsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("titulo", "Crear Nuevo Usuario");
        return "usuarios/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        
        // Validar email único
        if (usuario.getId() == null && usuarioService.existeEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Este email ya está registrado");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("roles", rolService.listarTodos());
            model.addAttribute("titulo", usuario.getId() == null ? "Crear Nuevo Usuario" : "Editar Usuario");
            return "usuarios/formulario";
        }
        
        if (usuario.getId() == null) {
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } else {
            usuarioService.actualizar(usuario.getId(), usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        }
        
        return "redirect:/usuarios";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    usuario.setPassword(""); // No mostrar contraseña
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("roles", rolService.listarTodos());
                    model.addAttribute("titulo", "Editar Usuario");
                    model.addAttribute("editando", true);
                    return "usuarios/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/usuarios";
                });
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "usuarios/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/usuarios";
                });
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
