package com.plataforma.academica.service;

import com.plataforma.academica.domain.Rol;
import java.util.List;
import java.util.Optional;

public interface RolService {
    
    List<Rol> listarTodos();
    
    Optional<Rol> buscarPorId(Long id);
    
    Optional<Rol> buscarPorNombre(String nombre);
    
    Rol guardar(Rol rol);
    
    Rol actualizar(Long id, Rol rol);
    
    void eliminar(Long id);
    
    boolean existeNombre(String nombre);
    
    boolean existeNombreExcluyendoId(String nombre, Long id);
}
