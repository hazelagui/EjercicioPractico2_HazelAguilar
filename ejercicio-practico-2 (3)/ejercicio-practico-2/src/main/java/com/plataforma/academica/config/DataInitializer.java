package com.plataforma.academica.config;

import com.plataforma.academica.domain.Rol;
import com.plataforma.academica.domain.Usuario;
import com.plataforma.academica.repository.RolRepository;
import com.plataforma.academica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) {
        // Solo inicializar si no hay datos
        if (rolRepository.count() == 0) {
            System.out.println("Inicializando datos de prueba...");
            
            // Crear roles
            Rol admin = new Rol("ADMIN", "Administrador del sistema");
            Rol profesor = new Rol("PROFESOR", "Usuario con permisos de consulta");
            Rol estudiante = new Rol("ESTUDIANTE", "Acceso limitado a información personal");
            
            admin = rolRepository.save(admin);
            profesor = rolRepository.save(profesor);
            estudiante = rolRepository.save(estudiante);
            
            System.out.println("Roles creados correctamente");
            
            // Crear usuarios de prueba con contraseñas encriptadas
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNombre("Carlos");
            usuarioAdmin.setApellido("Ramírez");
            usuarioAdmin.setEmail("c.ramirez@correo.com");
            usuarioAdmin.setPassword(passwordEncoder.encode("12345"));
            usuarioAdmin.setActivo(true);
            usuarioAdmin.setRol(admin);
            usuarioRepository.save(usuarioAdmin);
            
            Usuario usuarioEstudiante = new Usuario();
            usuarioEstudiante.setNombre("Ana");
            usuarioEstudiante.setApellido("Soto");
            usuarioEstudiante.setEmail("ana.soto@correo.com");
            usuarioEstudiante.setPassword(passwordEncoder.encode("12345"));
            usuarioEstudiante.setActivo(true);
            usuarioEstudiante.setRol(estudiante);
            usuarioRepository.save(usuarioEstudiante);
            
            Usuario usuarioProfesor = new Usuario();
            usuarioProfesor.setNombre("Luisa");
            usuarioProfesor.setApellido("Fernández");
            usuarioProfesor.setEmail("l.fernandez@correo.com");
            usuarioProfesor.setPassword(passwordEncoder.encode("12345"));
            usuarioProfesor.setActivo(true);
            usuarioProfesor.setRol(profesor);
            usuarioRepository.save(usuarioProfesor);
            
            System.out.println("Usuarios creados correctamente");
            System.out.println("==========================================");
            System.out.println("USUARIOS DE PRUEBA:");
            System.out.println("Admin: c.ramirez@correo.com / 12345");
            System.out.println("Profesor: l.fernandez@correo.com / 12345");
            System.out.println("Estudiante: ana.soto@correo.com / 12345");
            System.out.println("==========================================");
        }
    }
}
