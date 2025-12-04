package com.plataforma.academica.service;

import com.plataforma.academica.domain.Usuario;
import com.plataforma.academica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    public Usuario guardar(Usuario usuario) {
        // Encriptar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setApellido(usuarioActualizado.getApellido());
                    usuario.setEmail(usuarioActualizado.getEmail());
                    usuario.setActivo(usuarioActualizado.getActivo());
                    usuario.setRol(usuarioActualizado.getRol());
                    
                    // Solo actualizar contraseña si se proporciona una nueva
                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
                    }
                    
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
    
    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeEmailExcluyendoId(String email, Long id) {
        return usuarioRepository.existsByEmailAndIdNot(email, id);
    }
    
    // ============ CONSULTAS AVANZADAS ============
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRol(String nombreRol) {
        return usuarioRepository.findByRolNombre(nombreRol);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return usuarioRepository.findUsuariosEnRangoFechas(inicio, fin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorTermino(String termino) {
        return usuarioRepository.buscarPorTermino(termino);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> contarPorEstado() {
        Map<String, Long> resultado = new HashMap<>();
        resultado.put("activos", usuarioRepository.countByActivo(true));
        resultado.put("inactivos", usuarioRepository.countByActivo(false));
        return resultado;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarOrdenadosPorFecha(boolean descendente) {
        if (descendente) {
            return usuarioRepository.findAllByOrderByFechaCreacionDesc();
        }
        return usuarioRepository.findAllByOrderByFechaCreacionAsc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> contarPorRol() {
        Map<String, Long> resultado = new HashMap<>();
        List<Object[]> datos = usuarioRepository.contarUsuariosPorRol();
        for (Object[] fila : datos) {
            resultado.put((String) fila[0], (Long) fila[1]);
        }
        return resultado;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosRecientes(int dias) {
        LocalDateTime fecha = LocalDateTime.now().minusDays(dias);
        return usuarioRepository.findUsuariosRecientes(fecha);
    }
}
