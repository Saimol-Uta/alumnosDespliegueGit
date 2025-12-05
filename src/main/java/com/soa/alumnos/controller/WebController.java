package com.soa.alumnos.controller;

import com.soa.alumnos.dto.*;
import com.soa.alumnos.services.AlumnoService;
import com.soa.alumnos.services.CursoService;
import com.soa.alumnos.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final AlumnoService alumnoService;
    private final CursoService cursoService;
    private final UsuarioService usuarioService;

    public WebController(AlumnoService alumnoService, CursoService cursoService, UsuarioService usuarioService) {
        this.alumnoService = alumnoService;
        this.cursoService = cursoService;
        this.usuarioService = usuarioService;
    }

    // ========== Páginas principales ==========

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDto("", "", "", ""));
        model.addAttribute("esPrimerUsuario", usuarioService.contarUsuarios() == 0);
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") RegistroUsuarioDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            usuarioService.registrar(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Por favor inicie sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalAlumnos", alumnoService.listar().size());
        model.addAttribute("totalCursos", cursoService.listar().size());
        return "dashboard";
    }

    // ========== Gestión de Alumnos ==========

    @GetMapping("/alumnos")
    public String listarAlumnos(Model model) {
        model.addAttribute("alumnos", alumnoService.listarConCurso());
        model.addAttribute("cursos", cursoService.listar());
        return "alumnos/lista";
    }

    @GetMapping("/alumnos/nuevo")
    public String nuevoAlumno(Model model) {
        model.addAttribute("alumno", new AlumnoCreateDto("", "", "", "", ""));
        model.addAttribute("cursos", cursoService.listar());
        return "alumnos/formulario";
    }

    @PostMapping("/alumnos/nuevo")
    public String guardarAlumno(@Valid @ModelAttribute("alumno") AlumnoCreateDto dto,
            BindingResult result,
            @RequestParam(required = false) Long cursoId,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoService.listar());
            return "alumnos/formulario";
        }
        try {
            alumnoService.crear(dto);
            if (cursoId != null) {
                alumnoService.asignarCurso(dto.cedula(), cursoId);
            }
            redirectAttributes.addFlashAttribute("mensaje", "Alumno creado exitosamente");
            return "redirect:/alumnos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/alumnos/nuevo";
        }
    }

    @GetMapping("/alumnos/editar/{cedula}")
    public String editarAlumno(@PathVariable String cedula, Model model) {
        var alumno = alumnoService.porCedulaConCurso(cedula);
        model.addAttribute("alumno", alumno);
        model.addAttribute("alumnoUpdate",
                new AlumnoUpdateDto(alumno.nombre(), alumno.apellido(), alumno.direccion(), alumno.telefono()));
        model.addAttribute("cursos", cursoService.listar());
        model.addAttribute("esEdicion", true);
        return "alumnos/editar";
    }

    @PostMapping("/alumnos/editar/{cedula}")
    public String actualizarAlumno(@PathVariable String cedula,
            @Valid @ModelAttribute("alumnoUpdate") AlumnoUpdateDto dto,
            BindingResult result,
            @RequestParam(required = false) Long cursoId,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoService.listar());
            model.addAttribute("esEdicion", true);
            return "alumnos/editar";
        }
        try {
            alumnoService.actualizar(cedula, dto);
            alumnoService.asignarCurso(cedula, cursoId);
            redirectAttributes.addFlashAttribute("mensaje", "Alumno actualizado exitosamente");
            return "redirect:/alumnos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/alumnos/editar/" + cedula;
        }
    }

    @GetMapping("/alumnos/eliminar/{cedula}")
    public String eliminarAlumno(@PathVariable String cedula, RedirectAttributes redirectAttributes) {
        try {
            alumnoService.eliminar(cedula);
            redirectAttributes.addFlashAttribute("mensaje", "Alumno eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alumnos";
    }

    @GetMapping("/alumnos/{cedula}")
    public String verAlumno(@PathVariable String cedula, Model model) {
        model.addAttribute("alumno", alumnoService.porCedulaConCurso(cedula));
        return "alumnos/detalle";
    }

    // ========== Gestión de Cursos ==========

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoService.listarConAlumnos());
        model.addAttribute("alumnosDisponibles", alumnoService.listar().stream()
                .filter(a -> a.getCurso() == null)
                .toList());
        model.addAttribute("todosAlumnos", alumnoService.listar());
        return "cursos/lista";
    }

    @GetMapping("/cursos/nuevo")
    public String nuevoCurso(Model model) {
        model.addAttribute("curso", new CursoCreateDto("", "", ""));
        return "cursos/formulario";
    }

    @PostMapping("/cursos/nuevo")
    public String guardarCurso(@Valid @ModelAttribute("curso") CursoCreateDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cursos/formulario";
        }
        try {
            cursoService.crear(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Curso creado exitosamente");
            return "redirect:/cursos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cursos/nuevo";
        }
    }

    @GetMapping("/cursos/editar/{id}")
    public String editarCurso(@PathVariable Long id, Model model) {
        var curso = cursoService.porId(id);
        model.addAttribute("curso", curso);
        model.addAttribute("cursoUpdate", new CursoUpdateDto(curso.getNombre(), curso.getDescripcion()));
        model.addAttribute("esEdicion", true);
        return "cursos/editar";
    }

    @PostMapping("/cursos/editar/{id}")
    public String actualizarCurso(@PathVariable Long id,
            @Valid @ModelAttribute("cursoUpdate") CursoUpdateDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("esEdicion", true);
            return "cursos/editar";
        }
        try {
            cursoService.actualizar(id, dto);
            redirectAttributes.addFlashAttribute("mensaje", "Curso actualizado exitosamente");
            return "redirect:/cursos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cursos/editar/" + id;
        }
    }

    @GetMapping("/cursos/eliminar/{id}")
    public String eliminarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Curso eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/cursos/{id}")
    public String verCurso(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.porIdConAlumnos(id));
        model.addAttribute("alumnosDisponibles", alumnoService.listar().stream()
                .filter(a -> a.getCurso() == null)
                .toList());
        return "cursos/detalle";
    }

    @PostMapping("/cursos/{id}/asignar-alumno")
    public String asignarAlumnoACurso(@PathVariable Long id,
            @RequestParam String cedula,
            RedirectAttributes redirectAttributes) {
        try {
            cursoService.asignarAlumno(id, cedula);
            redirectAttributes.addFlashAttribute("mensaje", "Alumno asignado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("/cursos/{id}/desasignar-alumno/{cedula}")
    public String desasignarAlumnoDeCurso(@PathVariable Long id,
            @PathVariable String cedula,
            RedirectAttributes redirectAttributes) {
        try {
            cursoService.desasignarAlumno(cedula);
            redirectAttributes.addFlashAttribute("mensaje", "Alumno desasignado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cursos/" + id;
    }
}
