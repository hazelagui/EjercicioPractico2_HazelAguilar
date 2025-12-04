package com.plataforma.academica.controllers;

import com.plataforma.academica.service.RolService;
import com.plataforma.academica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    
    private final UsuarioService usuarioService;
    private final RolService rolService;
    
    @Autowired
    public ConsultaController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    
    @GetMapping
    public String mostrarConsultas(Model model) {
        // Cargar datos para los formularios de búsqueda
        model.addAttribute("roles", rolService.listarTodos());
        
        // Estadísticas generales
        model.addAttribute("conteoEstado", usuarioService.contarPorEstado());
        model.addAttribute("conteoPorRol", usuarioService.contarPorRol());
        
        return "consultas/index";
    }
    
    // 1. Buscar usuarios por rol
    @GetMapping("/por-rol")
    public String buscarPorRol(@RequestParam(required = false) String rol, Model model) {
        model.addAttribute("roles", rolService.listarTodos());
        
        if (rol != null && !rol.isEmpty()) {
            model.addAttribute("usuarios", usuarioService.buscarPorRol(rol));
            model.addAttribute("rolSeleccionado", rol);
        }
        
        model.addAttribute("tipoConsulta", "por-rol");
        return "consultas/resultados";
    }
    
    // 2. Buscar usuarios por rango de fechas
    @GetMapping("/por-fechas")
    public String buscarPorFechas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        
        if (fechaInicio != null && fechaFin != null) {
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
            model.addAttribute("usuarios", usuarioService.buscarPorRangoFechas(inicio, fin));
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
        }
        
        model.addAttribute("tipoConsulta", "por-fechas");
        return "consultas/resultados";
    }
    
    // 3. Buscar usuarios por término (nombre o email)
    @GetMapping("/buscar")
    public String buscarPorTermino(@RequestParam(required = false) String termino, Model model) {
        if (termino != null && !termino.isEmpty()) {
            model.addAttribute("usuarios", usuarioService.buscarPorTermino(termino));
            model.addAttribute("terminoBusqueda", termino);
        }
        
        model.addAttribute("tipoConsulta", "buscar");
        return "consultas/resultados";
    }
    
    // 4. Conteo de usuarios activos vs inactivos
    @GetMapping("/estadisticas")
    public String verEstadisticas(Model model) {
        model.addAttribute("conteoEstado", usuarioService.contarPorEstado());
        model.addAttribute("conteoPorRol", usuarioService.contarPorRol());
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        
        model.addAttribute("tipoConsulta", "estadisticas");
        return "consultas/estadisticas";
    }
    
    // 5. Usuarios ordenados por fecha de creación
    @GetMapping("/ordenados")
    public String listarOrdenados(@RequestParam(defaultValue = "desc") String orden, Model model) {
        boolean descendente = "desc".equalsIgnoreCase(orden);
        model.addAttribute("usuarios", usuarioService.listarOrdenadosPorFecha(descendente));
        model.addAttribute("ordenActual", orden);
        
        model.addAttribute("tipoConsulta", "ordenados");
        return "consultas/resultados";
    }
    
    // Usuarios recientes
    @GetMapping("/recientes")
    public String listarRecientes(@RequestParam(defaultValue = "7") int dias, Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuariosRecientes(dias));
        model.addAttribute("diasSeleccionados", dias);
        
        model.addAttribute("tipoConsulta", "recientes");
        return "consultas/resultados";
    }
}
