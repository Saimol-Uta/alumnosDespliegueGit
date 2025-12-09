package com.soa.alumnos.services;

import com.soa.alumnos.dto.CursoCreateDto;
import com.soa.alumnos.dto.CursoResponseDto;
import com.soa.alumnos.dto.CursoUpdateDto;
import com.soa.alumnos.dto.AlumnoResponseDto;
import com.soa.alumnos.entity.Alumno;
import com.soa.alumnos.entity.Curso;
import com.soa.alumnos.repository.AlumnoRepository;
import com.soa.alumnos.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepo;
    private final AlumnoRepository alumnoRepo;

    public CursoService(CursoRepository cursoRepo, AlumnoRepository alumnoRepo) {
        this.cursoRepo = cursoRepo;
        this.alumnoRepo = alumnoRepo;
    }

    public List<Curso> listar() {
        return cursoRepo.findAll();
    }

    public List<CursoResponseDto> listarConAlumnos() {
        return cursoRepo.findAll().stream()
                .map(CursoResponseDto::fromEntity)
                .toList();
    }

    public Curso crear(CursoCreateDto dto) {
        if (cursoRepo.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe un curso con el código: " + dto.codigo());
        }

        Curso curso = Curso.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .codigo(dto.codigo())
                .build();
        return cursoRepo.save(curso);
    }

    public Curso actualizar(Long id, CursoUpdateDto dto) {
        Curso curso = porId(id);
        curso.setNombre(dto.nombre());
        curso.setDescripcion(dto.descripcion());
        return cursoRepo.save(curso);
    }

    public Curso porId(Long id) {
        return cursoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con id: " + id));
    }

    public CursoResponseDto porIdConAlumnos(Long id) {
        Curso curso = porId(id);
        return CursoResponseDto.fromEntity(curso);
    }

    public void eliminar(Long id) {
        Curso curso = porId(id);
        // Desasociar alumnos antes de eliminar
        curso.getAlumnos().forEach(alumno -> alumno.setCurso(null));
        alumnoRepo.saveAll(curso.getAlumnos());
        cursoRepo.delete(curso);
    }

    @Transactional
    public AlumnoResponseDto asignarAlumno(Long cursoId, String cedulaAlumno) {
        Curso curso = porId(cursoId);
        Alumno alumno = alumnoRepo.findById(cedulaAlumno)
                .orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado con cédula: " + cedulaAlumno));

        alumno.setCurso(curso);
        alumnoRepo.save(alumno);

        return AlumnoResponseDto.fromEntity(alumno);
    }

    @Transactional
    public int asignarAlumnos(Long cursoId, List<String> cedulas) {
        Curso curso = porId(cursoId);
        int asignados = 0;
        
        for (String cedula : cedulas) {
            try {
                Alumno alumno = alumnoRepo.findById(cedula)
                        .orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado con cédula: " + cedula));
                
                alumno.setCurso(curso);
                alumnoRepo.save(alumno);
                asignados++;
            } catch (EntityNotFoundException e) {
                // Continuar con el siguiente alumno si uno no se encuentra
                continue;
            }
        }
        
        return asignados;
    }

    @Transactional
    public AlumnoResponseDto desasignarAlumno(String cedulaAlumno) {
        Alumno alumno = alumnoRepo.findById(cedulaAlumno)
                .orElseThrow(() -> new EntityNotFoundException("Alumno no encontrado con cédula: " + cedulaAlumno));

        alumno.setCurso(null);
        alumnoRepo.save(alumno);

        return AlumnoResponseDto.fromEntity(alumno);
    }

    public List<AlumnoResponseDto> obtenerAlumnosPorCurso(Long cursoId) {
        Curso curso = porId(cursoId);
        return alumnoRepo.findByCurso(curso).stream()
                .map(AlumnoResponseDto::fromEntity)
                .toList();
    }

    public List<CursoResponseDto> buscarPorCodigo(String codigo) {
        return cursoRepo.findByCodigoContainingIgnoreCase(codigo).stream()
                .map(CursoResponseDto::fromEntity)
                .toList();
    }
}
