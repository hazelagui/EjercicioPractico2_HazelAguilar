package com.plataforma.academica.repository;

import com.plataforma.academica.domain.Rol;
import com.plataforma.academica.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Búsqueda por email para autenticación
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);
    
    // ============ CONSULTAS AVANZADAS JPA ============
    
    // 1. Buscar usuarios por rol
    List<Usuario> findByRol(Rol rol);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :nombreRol")
    List<Usuario> findByRolNombre(@Param("nombreRol") String nombreRol);
    
    // 2. Buscar usuarios creados en un rango de fechas
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= :inicio AND u.fechaCreacion <= :fin ORDER BY u.fechaCreacion DESC")
    List<Usuario> findUsuariosEnRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    // 3. Buscar usuarios por coincidencia parcial en correo o nombre
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Usuario> buscarPorTermino(@Param("termino") String termino);
    
    List<Usuario> findByEmailContainingIgnoreCase(String email);
    
    List<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    
    // 4. Contar usuarios activos vs inactivos
    Long countByActivo(Boolean activo);
    
    @Query("SELECT u.activo, COUNT(u) FROM Usuario u GROUP BY u.activo")
    List<Object[]> contarUsuariosPorEstado();
    
    // 5. Obtener usuarios ordenados por fecha de creación
    List<Usuario> findAllByOrderByFechaCreacionDesc();
    
    List<Usuario> findAllByOrderByFechaCreacionAsc();
    
    // 6. Consultas adicionales útiles
    @Query("SELECT u.rol.nombre, COUNT(u) FROM Usuario u GROUP BY u.rol.nombre")
    List<Object[]> contarUsuariosPorRol();
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombre ASC")
    List<Usuario> findUsuariosActivosOrdenadosPorNombre();
    
    // Usuarios recientes (últimos N días)
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= :fecha ORDER BY u.fechaCreacion DESC")
    List<Usuario> findUsuariosRecientes(@Param("fecha") LocalDateTime fecha);
}
