package com.plataforma.academica.service;

import com.plataforma.academica.domain.Rol;
import com.plataforma.academica.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements RolService {
    
    private final RolRepository rolRepository;
    
    @Autowired
    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
    
    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }
    
    @Override
    public Rol actualizar(Long id, Rol rolActualizado) {
        return rolRepository.findById(id)
                .map(rol -> {
                    rol.setNombre(rolActualizado.getNombre());
                    rol.setDescripcion(rolActualizado.getDescripcion());
                    return rolRepository.save(rol);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        rolRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeNombreExcluyendoId(String nombre, Long id) {
        return rolRepository.existsByNombreAndIdNot(nombre, id);
    }
}
