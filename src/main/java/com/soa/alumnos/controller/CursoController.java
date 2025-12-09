package com.soa.alumnos.controller;

import com.soa.alumnos.dto.*;
import com.soa.alumnos.entity.Curso;
import com.soa.alumnos.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cursos")
@CrossOrigin(origins = { "*" })
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CursoResponseDto> listar() {
        return service.listarConAlumnos();
    }

    @GetMapping("/{id}")
    public CursoResponseDto porId(@PathVariable Long id) {
        return service.porIdConAlumnos(id);
    }

    // Buscar curso por código
    @GetMapping("/buscar")
    public List<CursoResponseDto> buscarPorCodigo(@RequestParam String codigo) {
        return service.buscarPorCodigo(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Curso crear(@Valid @RequestBody CursoCreateDto dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public Curso actualizar(@PathVariable Long id, @Valid @RequestBody CursoUpdateDto dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Obtener todos los estudiantes que pertenecen a un curso
    @GetMapping("/{id}/alumnos")
    public List<AlumnoResponseDto> obtenerAlumnosPorCurso(@PathVariable Long id) {
        return service.obtenerAlumnosPorCurso(id);
    }

    // Asignar un estudiante a un curso
    @PostMapping("/{id}/alumnos/{cedula}")
    public AlumnoResponseDto asignarAlumno(@PathVariable Long id, @PathVariable String cedula) {
        return service.asignarAlumno(id, cedula);
    }

    // Desasignar un estudiante de su curso
    @DeleteMapping("/alumnos/{cedula}")
    public AlumnoResponseDto desasignarAlumno(@PathVariable String cedula) {
        return service.desasignarAlumno(cedula);
    }
}
