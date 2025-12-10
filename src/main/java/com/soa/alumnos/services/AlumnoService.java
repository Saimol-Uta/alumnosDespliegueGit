package com.soa.alumnos.services;

import com.soa.alumnos.dto.AlumnoCreateDto;
import com.soa.alumnos.dto.AlumnoResponseDto;
import com.soa.alumnos.dto.AlumnoUpdateDto;
import com.soa.alumnos.entity.Alumno;
import com.soa.alumnos.entity.Curso;
import com.soa.alumnos.repository.AlumnoRepository;
import com.soa.alumnos.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository repo;
    private final CursoRepository cursoRepo;

    public AlumnoService(AlumnoRepository repo, CursoRepository cursoRepo) {
        this.repo = repo;
        this.cursoRepo = cursoRepo;
    }

    public List<Alumno> listar() {
        return repo.findAll();
    }

    public List<AlumnoResponseDto> listarConCurso() {
        return repo.findAll().stream()
                .map(AlumnoResponseDto::fromEntity)
                .toList();
    }

    public Alumno crear(AlumnoCreateDto alumnoCreateDto) {
        if (repo.existsBycedula(alumnoCreateDto.cedula())) {
            throw new IllegalArgumentException("El alumno con cedula " + alumnoCreateDto.cedula() + " ya existe");
        }

        Alumno alumno = Alumno.builder()
                .cedula(alumnoCreateDto.cedula())
                .nombre(alumnoCreateDto.nombre())
                .apellido(alumnoCreateDto.apellido())
                .direccion(alumnoCreateDto.direccion())
                .telefono(alumnoCreateDto.telefono())
                .build();
        return repo.save(alumno);
    }

    public Alumno actualizar(String cedula, AlumnoUpdateDto alumnoUpdateDto) {
        Alumno a = porCedula(cedula);
        a.setNombre(alumnoUpdateDto.nombre());
        a.setApellido(alumnoUpdateDto.apellido());
        a.setDireccion(alumnoUpdateDto.direccion());
        a.setTelefono(alumnoUpdateDto.telefono());
        return repo.save(a);
    }

    public Alumno porCedula(String cedula) {
        return repo.findById(cedula).orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado"));
    }

    public AlumnoResponseDto porCedulaConCurso(String cedula) {
        Alumno alumno = porCedula(cedula);
        return AlumnoResponseDto.fromEntity(alumno);
    }

    public void eliminar(String cedula) {
        Alumno a = porCedula(cedula);
        repo.delete(a);
    }

    @Transactional
    public AlumnoResponseDto asignarCurso(String cedula, Long cursoId) {
        Alumno alumno = porCedula(cedula);

        if (cursoId == null) {
            alumno.setCurso(null);
        } else {
            Curso curso = cursoRepo.findById(cursoId)
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con id: " + cursoId));
            alumno.setCurso(curso);
        }

        repo.save(alumno);
        return AlumnoResponseDto.fromEntity(alumno);
    }

    public AlumnoResponseDto.CursoSimpleDto consultarCurso(String cedula) {
        Alumno alumno = porCedula(cedula);
        if (alumno.getCurso() == null) {
            return null;
        }
        return new AlumnoResponseDto.CursoSimpleDto(
                alumno.getCurso().getId(),
                alumno.getCurso().getNombre(),
                alumno.getCurso().getCodigo());
    }

    public List<AlumnoResponseDto> buscarPorCedula(String cedula) {
        return repo.findByCedulaContaining(cedula).stream()
                .map(AlumnoResponseDto::fromEntity)
                .toList();
    }
}
