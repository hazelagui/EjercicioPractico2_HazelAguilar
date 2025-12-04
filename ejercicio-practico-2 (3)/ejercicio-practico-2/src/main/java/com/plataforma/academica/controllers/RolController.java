package com.plataforma.academica.controllers;

import com.plataforma.academica.domain.Rol;
import com.plataforma.academica.service.RolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/roles")
public class RolController {
    
    private final RolService rolService;
    
    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        return "roles/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("titulo", "Crear Nuevo Rol");
        return "roles/formulario";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("rol") Rol rol,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        
        // Validar nombre único
        if (rol.getId() == null && rolService.existeNombre(rol.getNombre())) {
            result.rejectValue("nombre", "error.rol", "Este nombre de rol ya existe");
        } else if (rol.getId() != null && rolService.existeNombreExcluyendoId(rol.getNombre(), rol.getId())) {
            result.rejectValue("nombre", "error.rol", "Este nombre de rol ya existe");
        }
        
        if (result.hasErrors()) {
            model.addAttribute("titulo", rol.getId() == null ? "Crear Nuevo Rol" : "Editar Rol");
            return "roles/formulario";
        }
        
        if (rol.getId() == null) {
            rolService.guardar(rol);
            redirectAttributes.addFlashAttribute("mensaje", "Rol creado exitosamente");
        } else {
            rolService.actualizar(rol.getId(), rol);
            redirectAttributes.addFlashAttribute("mensaje", "Rol actualizado exitosamente");
        }
        
        return "redirect:/roles";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return rolService.buscarPorId(id)
                .map(rol -> {
                    model.addAttribute("rol", rol);
                    model.addAttribute("titulo", "Editar Rol");
                    return "roles/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Rol no encontrado");
                    return "redirect:/roles";
                });
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rol eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el rol porque tiene usuarios asignados");
        }
        return "redirect:/roles";
    }
}
