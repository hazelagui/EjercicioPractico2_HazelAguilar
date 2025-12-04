# Ejercicio Práctico 2 - Plataforma Académica

## Descripción
Sistema de Gestión de Usuarios y Roles para una Plataforma Académica desarrollado con Spring Boot.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **MySQL**
- **Thymeleaf**
- **Lombok**

## Estructura del Proyecto

```
src/main/java/com/plataforma/academica/
├── domain/           # Entidades JPA (Usuario, Rol)
├── repository/       # Repositorios JPA con consultas avanzadas
├── service/          # Interfaces e implementaciones de servicios
├── controllers/      # Controladores web
└── config/          # Configuraciones (Security, DataInitializer)

src/main/resources/
├── templates/        # Plantillas Thymeleaf
│   ├── fragments/    # Header, Navbar, Footer, Layout
│   ├── auth/        # Login, Acceso denegado
│   ├── admin/       # Dashboard admin
│   ├── usuarios/    # CRUD de usuarios
│   ├── roles/       # CRUD de roles
│   ├── consultas/   # Consultas avanzadas
│   ├── profesor/    # Reportes
│   └── estudiante/  # Perfil
├── static/css/      # Estilos CSS
└── application.properties
```

## Configuración de Base de Datos

### Requisitos
- MySQL Server instalado
- Base de datos `plataforma` creada

### Script de la Base de Datos
Ejecutar el script `creaTabla_Usuarios.sql` proporcionado para crear la base de datos y las tablas.

### Configuración en application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plataforma
spring.datasource.username=root
spring.datasource.password=root
```
**Nota:** Modifica las credenciales según tu configuración local.

## Ejecución

### Opción 1: Maven
```bash
mvn spring-boot:run
```

### Opción 2: JAR
```bash
mvn clean package
java -jar target/academica-1.0.0.jar
```

La aplicación se ejecuta en: **http://localhost:78**

## Usuarios de Prueba

| Rol | Email | Contraseña |
|-----|-------|------------|
| ADMIN | c.ramirez@correo.com | 12345 |
| PROFESOR | l.fernandez@correo.com | 12345 |
| ESTUDIANTE | ana.soto@correo.com | 12345 |

## Funcionalidades

### 1. Autenticación y Autorización
- Login/Logout con Spring Security
- Redirección según rol
- Restricción de URLs por rol

### 2. Gestión de Usuarios (ADMIN)
- Listar usuarios
- Crear usuario con asignación de rol
- Editar usuario
- Eliminar usuario
- Ver detalle de usuario

### 3. Gestión de Roles (ADMIN)
- CRUD completo de roles
- Validación de nombre único
- Control de eliminación (no permite si tiene usuarios)

### 4. Consultas Avanzadas JPA (ADMIN)
1. Buscar usuarios por rol
2. Buscar usuarios por rango de fechas
3. Buscar usuarios por nombre o email (coincidencia parcial)
4. Conteo de usuarios activos vs inactivos
5. Usuarios ordenados por fecha de creación
6. Usuarios recientes (últimos N días)

### 5. Reportes (PROFESOR)
- Estadísticas generales
- Lista de estudiantes
- Usuarios recientes

### 6. Perfil (ESTUDIANTE)
- Ver información personal

## Control de Acceso

| Ruta | ADMIN | PROFESOR | ESTUDIANTE |
|------|-------|----------|------------|
| /admin/** | ✅ | ❌ | ❌ |
| /usuarios/** | ✅ | ❌ | ❌ |
| /roles/** | ✅ | ❌ | ❌ |
| /consultas/** | ✅ | ❌ | ❌ |
| /profesor/** | ❌ | ✅ | ❌ |
| /estudiante/** | ❌ | ❌ | ✅ |

## Interfaz de Usuario
- Header con información del usuario
- Navbar dinámico según rol
- Footer informativo
- Diseño responsive
- Alertas de éxito/error
- Confirmación de eliminación

## Autor
Universidad Fidélitas - Ingeniería en Sistemas de Computación
Desarrollo de Aplicaciones Web y Patrones
