package com.soa.alumnos.controller;

import com.soa.alumnos.dto.AlumnoCreateDto;
import com.soa.alumnos.dto.AlumnoResponseDto;
import com.soa.alumnos.dto.AlumnoUpdateDto;
import com.soa.alumnos.entity.Alumno;
import com.soa.alumnos.services.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/alumnos")
@CrossOrigin(origins = { "*" })
public class AlumnosController {

    private final AlumnoService service;

    public AlumnosController(AlumnoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlumnoResponseDto> listar() {
        return service.listarConCurso();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alumno crearAlumno(@Valid @RequestBody AlumnoCreateDto alumnoCreateDto) {
        return service.crear(alumnoCreateDto);
    }

    @PutMapping("/{cedula}")
    public Alumno actualizarAlumno(@PathVariable String cedula, @Valid @RequestBody AlumnoUpdateDto alumnoUpdateDto) {
        return service.actualizar(cedula, alumnoUpdateDto);
    }

    @GetMapping("/{cedula}")
    public AlumnoResponseDto porCedula(@PathVariable String cedula) {
        return service.porCedulaConCurso(cedula);
    }

    // Buscar alumnos por cédula (búsqueda parcial)
    @GetMapping("/buscar")
    public List<AlumnoResponseDto> buscarPorCedula(@RequestParam String cedula) {
        return service.buscarPorCedula(cedula);
    }

    @DeleteMapping("/{cedula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarAlumno(@PathVariable String cedula) {
        service.eliminar(cedula);
    }

    // Consultar el curso al que pertenece un estudiante
    @GetMapping("/{cedula}/curso")
    public AlumnoResponseDto.CursoSimpleDto consultarCurso(@PathVariable String cedula) {
        return service.consultarCurso(cedula);
    }

    // Asignar un curso a un estudiante
    @PutMapping("/{cedula}/curso/{cursoId}")
    public AlumnoResponseDto asignarCurso(@PathVariable String cedula, @PathVariable Long cursoId) {
        return service.asignarCurso(cedula, cursoId);
    }

    // Desasignar curso de un estudiante
    @DeleteMapping("/{cedula}/curso")
    public AlumnoResponseDto desasignarCurso(@PathVariable String cedula) {
        return service.asignarCurso(cedula, null);
    }
}
