package com.plataforma.academica.service;

import com.plataforma.academica.domain.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsuarioService {
    
    // CRUD básico
    List<Usuario> listarTodos();
    
    Optional<Usuario> buscarPorId(Long id);
    
    Optional<Usuario> buscarPorEmail(String email);
    
    Usuario guardar(Usuario usuario);
    
    Usuario actualizar(Long id, Usuario usuario);
    
    void eliminar(Long id);
    
    boolean existeEmail(String email);
    
    boolean existeEmailExcluyendoId(String email, Long id);
    
    // Consultas avanzadas
    List<Usuario> buscarPorRol(String nombreRol);
    
    List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
    
    List<Usuario> buscarPorTermino(String termino);
    
    Map<String, Long> contarPorEstado();
    
    List<Usuario> listarOrdenadosPorFecha(boolean descendente);
    
    Map<String, Long> contarPorRol();
    
    List<Usuario> listarUsuariosRecientes(int dias);
}
