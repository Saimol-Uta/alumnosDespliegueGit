# 📚 Sistema de Gestión de Alumnos y Cursos

Sistema web desarrollado con **Spring Boot** para la gestión académica de alumnos y cursos, con autenticación basada en roles, interfaz moderna con Bootstrap 5 y despliegue con Docker.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Modelo de Datos](#-modelo-de-datos)
- [Sistema de Roles y Permisos](#-sistema-de-roles-y-permisos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Endpoints API REST](#-endpoints-api-rest)
- [Vistas Web](#-vistas-web)
- [Configuración](#-configuración)
- [Funcionamiento del Código](#-funcionamiento-del-código)

---

## ✨ Características

### Funcionalidades Principales
- ✅ **CRUD completo de Alumnos** - Crear, leer, actualizar y eliminar estudiantes
- ✅ **CRUD completo de Cursos** - Gestión completa de cursos académicos
- ✅ **Asignación de Alumnos a Cursos** - Relación muchos a uno
- ✅ **Sistema de Autenticación** - Login y registro de usuarios
- ✅ **Control de Acceso por Roles** - ADMIN y SECRETARIA
- ✅ **Interfaz Moderna** - Diseño minimalista con modales interactivos
- ✅ **API RESTful** - Endpoints para integración con otros sistemas
- ✅ **Despliegue con Docker** - Configuración lista para producción

### Características de la Interfaz
- 🎨 Diseño minimalista con sidebar oscuro
- 📱 Responsive design con Bootstrap 5
- 🔲 Modales para crear, editar y ver registros
- ⚠️ Confirmación de eliminación con modal
- 👥 Asignación de estudiantes desde la lista de cursos

---

## 🏗 Arquitectura del Sistema

El sistema sigue una arquitectura de capas (Layered Architecture):

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                      │
│  ┌─────────────────┐  ┌─────────────────────────────────┐   │
│  │   Thymeleaf     │  │      REST Controllers           │   │
│  │   (Vistas Web)  │  │   (API JSON)                    │   │
│  └─────────────────┘  └─────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE NEGOCIO                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐  │
│  │  AlumnoService  │  │  CursoService   │  │ UsuarioSvc  │  │
│  └─────────────────┘  └─────────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE DATOS                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐  │
│  │  AlumnoRepo     │  │  CursoRepo      │  │ UsuarioRepo │  │
│  └─────────────────┘  └─────────────────┘  └─────────────┘  │
│                    Spring Data JPA                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       BASE DE DATOS                          │
│                         MySQL 8.0                            │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 21 | Lenguaje de programación principal |
| **Spring Boot** | 3.2.0 | Framework de aplicación |
| **Spring Security** | 6.x | Autenticación y autorización |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **Thymeleaf** | 3.x | Motor de plantillas HTML |
| **MySQL** | 8.0 | Base de datos relacional |
| **Bootstrap** | 5.3.2 | Framework CSS |
| **Lombok** | 1.18.x | Reducción de código boilerplate |
| **Docker** | 24.x | Contenedorización |
| **Maven** | 3.9.x | Gestión de dependencias |

---

## 📁 Estructura del Proyecto

```
src/main/java/com/soa/alumnos/
├── AlumnosApplication.java          # Clase principal de Spring Boot
│
├── config/
│   └── SecurityConfig.java          # Configuración de Spring Security
│
├── controller/
│   ├── AlumnosController.java       # API REST de Alumnos
│   ├── AuthController.java          # API REST de Autenticación
│   ├── CursoController.java         # API REST de Cursos
│   └── WebController.java           # Controlador de vistas Thymeleaf
│
├── dto/
│   ├── AlumnoCreateDto.java         # DTO para crear alumno
│   ├── AlumnoResponseDto.java       # DTO de respuesta de alumno
│   ├── AlumnoUpdateDto.java         # DTO para actualizar alumno
│   ├── AsignarCursoDto.java         # DTO para asignar curso
│   ├── CursoCreateDto.java          # DTO para crear curso
│   ├── CursoResponseDto.java        # DTO de respuesta de curso
│   ├── CursoUpdateDto.java          # DTO para actualizar curso
│   ├── LoginDto.java                # DTO de login
│   └── RegistroUsuarioDto.java      # DTO de registro
│
├── entity/
│   ├── Alumno.java                  # Entidad JPA de Alumno
│   ├── Curso.java                   # Entidad JPA de Curso
│   └── Usuario.java                 # Entidad JPA de Usuario
│
├── repository/
│   ├── AlumnoRepository.java        # Repositorio de Alumnos
│   ├── CursoRepository.java         # Repositorio de Cursos
│   └── UsuarioRepository.java       # Repositorio de Usuarios
│
└── services/
    ├── AlumnoService.java           # Lógica de negocio de Alumnos
    ├── CursoService.java            # Lógica de negocio de Cursos
    └── UsuarioService.java          # Lógica de negocio de Usuarios

src/main/resources/
├── application.properties           # Configuración de la aplicación
├── static/
│   ├── app.js                       # JavaScript de la aplicación
│   └── index.html                   # Página estática
└── templates/
    ├── dashboard.html               # Panel principal
    ├── login.html                   # Página de login
    ├── registro.html                # Página de registro
    ├── alumnos/
    │   ├── lista.html               # Lista de alumnos con modales
    │   ├── detalle.html             # Detalle de alumno
    │   ├── formulario.html          # Formulario de alumno
    │   └── editar.html              # Edición de alumno
    └── cursos/
        ├── lista.html               # Lista de cursos con modales
        ├── detalle.html             # Detalle de curso
        ├── formulario.html          # Formulario de curso
        └── editar.html              # Edición de curso
```

---

## 📊 Modelo de Datos

### Diagrama Entidad-Relación

```
┌─────────────────────┐         ┌─────────────────────┐
│       ALUMNO        │         │        CURSO        │
├─────────────────────┤         ├─────────────────────┤
│ cedula (PK) VARCHAR │    ┌───>│ id (PK) BIGINT      │
│ nombre VARCHAR(20)  │    │    │ codigo VARCHAR(20)  │
│ apellido VARCHAR(20)│    │    │ nombre VARCHAR(100) │
│ direccion VARCHAR(50│    │    │ descripcion VARCHAR │
│ telefono VARCHAR(10)│    │    │                     │
│ curso_id (FK) ──────┼────┘    │                     │
└─────────────────────┘         └─────────────────────┘
         N                                1
         
         Relación: Muchos Alumnos → Un Curso

┌─────────────────────┐
│      USUARIO        │
├─────────────────────┤
│ id (PK) BIGINT      │
│ username VARCHAR(50)│
│ password VARCHAR    │
│ email VARCHAR(100)  │
│ nombre VARCHAR(100) │
│ rol ENUM            │
│ activo BOOLEAN      │
└─────────────────────┘
```

### Descripción de Entidades

#### Alumno (`alumnosGrupo5`)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `cedula` | VARCHAR(10) | Cédula de identidad (PK) |
| `nombre` | VARCHAR(20) | Nombre del estudiante |
| `apellido` | VARCHAR(20) | Apellido del estudiante |
| `direccion` | VARCHAR(50) | Dirección domiciliaria |
| `telefono` | VARCHAR(10) | Número de teléfono |
| `curso_id` | BIGINT | FK al curso asignado (nullable) |

#### Curso (`cursos`)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | BIGINT | Identificador único (PK, auto-increment) |
| `codigo` | VARCHAR(20) | Código único del curso (ej: MAT101) |
| `nombre` | VARCHAR(100) | Nombre del curso |
| `descripcion` | VARCHAR(255) | Descripción del curso (opcional) |

#### Usuario (`usuarios`)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | BIGINT | Identificador único (PK) |
| `username` | VARCHAR(50) | Nombre de usuario (único) |
| `password` | VARCHAR | Contraseña encriptada (BCrypt) |
| `email` | VARCHAR(100) | Correo electrónico (único) |
| `nombre` | VARCHAR(100) | Nombre completo |
| `rol` | ENUM | ADMIN o SECRETARIA |
| `activo` | BOOLEAN | Estado del usuario |

---

## 🔐 Sistema de Roles y Permisos

### Roles Disponibles

| Rol | Descripción |
|-----|-------------|
| **ADMIN** | Acceso completo a todas las funcionalidades |
| **SECRETARIA** | Acceso solo a gestión de alumnos |

### Matriz de Permisos

| Funcionalidad | ADMIN | SECRETARIA |
|---------------|:-----:|:----------:|
| Ver Dashboard | ✅ | ✅ |
| Listar Alumnos | ✅ | ✅ |
| Crear Alumno | ✅ | ✅ |
| Editar Alumno | ✅ | ✅ |
| Eliminar Alumno | ✅ | ✅ |
| Ver Cursos | ✅ | ❌ |
| Crear Curso | ✅ | ❌ |
| Editar Curso | ✅ | ❌ |
| Eliminar Curso | ✅ | ❌ |
| Asignar Alumnos a Cursos | ✅ | ❌ |

### Primer Usuario como ADMIN

El sistema está configurado para que **el primer usuario que se registre sea automáticamente ADMIN**. Los usuarios siguientes serán SECRETARIA.

```java
// En UsuarioService.java
boolean esPrimerUsuario = usuarioRepo.count() == 0;
Usuario.Rol rol = esPrimerUsuario ? Usuario.Rol.ADMIN : Usuario.Rol.SECRETARIA;
```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos

- Docker y Docker Compose
- O alternativamente: Java 21, Maven 3.9+, MySQL 8.0

### Opción 1: Docker (Recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/alumnosDespliegueGit.git
cd alumnosDespliegueGit

# Construir y ejecutar con Docker Compose
docker-compose up -d --build

# Ver logs de la aplicación
docker-compose logs -f app

# Detener los contenedores
docker-compose down
```

La aplicación estará disponible en: `http://localhost:8080`

### Opción 2: Desarrollo Local

```bash
# 1. Configurar MySQL
mysql -u root -p
CREATE DATABASE soa;
EXIT;

# 2. Configurar variables de entorno (opcional)
export DATABASE_URL=jdbc:mysql://localhost:3306/soa
export DATABASE_USER=root
export DATABASE_PASSWORD=tu_password

# 3. Ejecutar la aplicación
./mvnw spring-boot:run
```

### Docker Compose - Servicios

```yaml
services:
  app:            # Aplicación Spring Boot (puerto 8080)
  mysql:          # Base de datos MySQL (puerto 3306)
```

---

## 📡 Endpoints API REST

### Alumnos (`/api/alumnos`)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| GET | `/api/alumnos` | Listar todos los alumnos | ADMIN, SECRETARIA |
| GET | `/api/alumnos/{cedula}` | Obtener alumno por cédula | ADMIN, SECRETARIA |
| POST | `/api/alumnos` | Crear nuevo alumno | ADMIN, SECRETARIA |
| PUT | `/api/alumnos/{cedula}` | Actualizar alumno | ADMIN, SECRETARIA |
| DELETE | `/api/alumnos/{cedula}` | Eliminar alumno | ADMIN, SECRETARIA |
| GET | `/api/alumnos/{cedula}/curso` | Obtener curso del alumno | ADMIN, SECRETARIA |
| PUT | `/api/alumnos/{cedula}/curso/{cursoId}` | Asignar curso | ADMIN, SECRETARIA |
| DELETE | `/api/alumnos/{cedula}/curso` | Desasignar curso | ADMIN, SECRETARIA |

### Cursos (`/api/cursos`)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| GET | `/api/cursos` | Listar todos los cursos | ADMIN |
| GET | `/api/cursos/{id}` | Obtener curso por ID | ADMIN |
| POST | `/api/cursos` | Crear nuevo curso | ADMIN |
| PUT | `/api/cursos/{id}` | Actualizar curso | ADMIN |
| DELETE | `/api/cursos/{id}` | Eliminar curso | ADMIN |
| GET | `/api/cursos/{id}/alumnos` | Obtener alumnos del curso | ADMIN |
| POST | `/api/cursos/{id}/alumnos/{cedula}` | Asignar alumno | ADMIN |
| DELETE | `/api/cursos/alumnos/{cedula}` | Desasignar alumno | ADMIN |

### Autenticación (`/api/auth`)

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| POST | `/api/auth/registro` | Registrar nuevo usuario | Público |

### Ejemplos de Uso con cURL

```bash
# Crear un alumno
curl -X POST http://localhost:8080/api/alumnos \
  -H "Content-Type: application/json" \
  -d '{
    "cedula": "1234567890",
    "nombre": "Juan",
    "apellido": "Pérez",
    "direccion": "Av. Principal 123",
    "telefono": "0991234567"
  }'

# Crear un curso
curl -X POST http://localhost:8080/api/cursos \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "MAT101",
    "nombre": "Matemáticas I",
    "descripcion": "Curso de matemáticas básicas"
  }'

# Asignar alumno a curso
curl -X POST http://localhost:8080/api/cursos/1/alumnos/1234567890
```

---

## 🖥 Vistas Web

### Rutas Públicas

| Ruta | Descripción |
|------|-------------|
| `/login` | Página de inicio de sesión |
| `/registro` | Formulario de registro de usuarios |

### Rutas Protegidas

| Ruta | Descripción | Roles |
|------|-------------|-------|
| `/dashboard` | Panel principal con estadísticas | ADMIN, SECRETARIA |
| `/alumnos` | Lista de alumnos con CRUD modal | ADMIN, SECRETARIA |
| `/alumnos/{cedula}` | Detalle de un alumno | ADMIN, SECRETARIA |
| `/cursos` | Lista de cursos con CRUD modal | ADMIN |
| `/cursos/{id}` | Detalle de un curso | ADMIN |

### Características de la Interfaz

#### Sidebar de Navegación
- Diseño oscuro (#111) fijo en el lado izquierdo
- Navegación contextual según el rol del usuario
- Los enlaces de Cursos solo son visibles para ADMIN

#### Modales Interactivos
1. **Modal Crear** - Formulario para nuevos registros
2. **Modal Editar** - Formulario prellenado para edición
3. **Modal Ver** - Visualización de detalles
4. **Modal Eliminar** - Confirmación antes de eliminar
5. **Modal Asignar** (solo Cursos) - Asignar estudiantes a un curso

---

## ⚙️ Configuración

### application.properties

```properties
# Servidor
server.port=8080
server.forward-headers-strategy=framework

# Base de datos
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/soa}
spring.datasource.username=${DATABASE_USER:root}
spring.datasource.password=${DATABASE_PASSWORD:}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `DATABASE_URL` | URL de conexión JDBC | `jdbc:mysql://localhost:3306/soa` |
| `DATABASE_USER` | Usuario de MySQL | `root` |
| `DATABASE_PASSWORD` | Contraseña de MySQL | (vacío) |
| `SERVER_PORT` | Puerto del servidor | `8080` |

---

## 🔧 Funcionamiento del Código

### 1. Capa de Entidades (Entity)

Las entidades representan las tablas de la base de datos usando JPA/Hibernate.

```java
// Alumno.java - Entidad principal de estudiantes
@Entity
@Table(name = "alumnosGrupo5")
public class Alumno {
    @Id
    private String cedula;           // Clave primaria
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;             // Relación con Curso
}

// Curso.java - Entidad de cursos
@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "curso")
    private List<Alumno> alumnos;    // Relación inversa
}
```

### 2. Capa de Repositorios (Repository)

Interfaces que extienden `JpaRepository` para operaciones CRUD automáticas.

```java
public interface AlumnoRepository extends JpaRepository<Alumno, String> {
    boolean existsBycedula(String cedula);
}

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByCodigo(String codigo);
}
```

### 3. Capa de Servicios (Service)

Contiene la lógica de negocio de la aplicación.

```java
@Service
public class AlumnoService {
    
    // Crear alumno con validación
    public Alumno crear(AlumnoCreateDto dto) {
        if (repo.existsBycedula(dto.cedula())) {
            throw new IllegalArgumentException("El alumno ya existe");
        }
        return repo.save(Alumno.builder()
            .cedula(dto.cedula())
            .nombre(dto.nombre())
            // ... más campos
            .build());
    }
    
    // Asignar curso a alumno
    @Transactional
    public AlumnoResponseDto asignarCurso(String cedula, Long cursoId) {
        Alumno alumno = porCedula(cedula);
        Curso curso = cursoRepo.findById(cursoId).orElseThrow();
        alumno.setCurso(curso);
        return AlumnoResponseDto.fromEntity(repo.save(alumno));
    }
}
```

### 4. Capa de Controladores (Controller)

#### WebController - Vistas Thymeleaf

```java
@Controller
public class WebController {
    
    @GetMapping("/alumnos")
    public String listarAlumnos(Model model) {
        model.addAttribute("alumnos", alumnoService.listarConCurso());
        model.addAttribute("cursos", cursoService.listar());
        return "alumnos/lista";  // → templates/alumnos/lista.html
    }
    
    @PostMapping("/alumnos/nuevo")
    public String guardarAlumno(@Valid AlumnoCreateDto dto, 
                                RedirectAttributes redirect) {
        alumnoService.crear(dto);
        redirect.addFlashAttribute("mensaje", "Alumno creado exitosamente");
        return "redirect:/alumnos";
    }
}
```

#### REST Controllers - API JSON

```java
@RestController
@RequestMapping("api/alumnos")
public class AlumnosController {
    
    @GetMapping
    public List<AlumnoResponseDto> listar() {
        return service.listarConCurso();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alumno crear(@Valid @RequestBody AlumnoCreateDto dto) {
        return service.crear(dto);
    }
}
```

### 5. Seguridad (SecurityConfig)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/login", "/registro").permitAll()
                // Solo ADMIN puede acceder a cursos
                .requestMatchers("/cursos/**").hasRole("ADMIN")
                // ADMIN y SECRETARIA pueden acceder a alumnos
                .requestMatchers("/alumnos/**").hasAnyRole("ADMIN", "SECRETARIA")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
            );
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Encriptación de contraseñas
    }
}
```

### 6. DTOs (Data Transfer Objects)

Records de Java para transferencia de datos:

```java
// Para crear un alumno (entrada)
public record AlumnoCreateDto(
    @NotBlank String cedula,
    @NotBlank String nombre,
    @NotBlank String apellido,
    String direccion,
    String telefono
) {}

// Para respuestas (salida)
public record AlumnoResponseDto(
    String cedula,
    String nombre,
    String apellido,
    String direccion,
    String telefono,
    CursoSimpleDto curso
) {
    public static AlumnoResponseDto fromEntity(Alumno a) {
        return new AlumnoResponseDto(
            a.getCedula(),
            a.getNombre(),
            // ... conversión de entidad a DTO
        );
    }
}
```

### 7. Vistas Thymeleaf

```html
<!-- lista.html - Ejemplo de modal -->
<button onclick="abrirModalEditar(this)"
        th:data-cedula="${alumno.cedula}"
        th:data-nombre="${alumno.nombre}">
    ✏️ Editar
</button>

<!-- Modal con Thymeleaf -->
<form th:action="@{/alumnos/nuevo}" method="post">
    <input type="text" name="cedula" required>
    <select name="cursoId">
        <option th:each="curso : ${cursos}" 
                th:value="${curso.id}" 
                th:text="${curso.nombre}">
        </option>
    </select>
</form>

<!-- JavaScript para modales dinámicos -->
<script>
function abrirModalEditar(btn) {
    document.getElementById('editNombre').value = btn.dataset.nombre;
    new bootstrap.Modal(document.getElementById('modalEditar')).show();
}
</script>
```

---

## 📝 Licencia

Este proyecto es de uso educativo desarrollado para el curso de Arquitectura Orientada a Servicios (SOA).

---

## 👥 Autores

Desarrollado por **Grupo 5** - Universidad Técnica de Ambato

---

## 🆘 Soporte

Si encuentras algún problema o tienes sugerencias:
1. Abre un issue en el repositorio
2. Describe el problema detalladamente
3. Incluye capturas de pantalla si es necesario
