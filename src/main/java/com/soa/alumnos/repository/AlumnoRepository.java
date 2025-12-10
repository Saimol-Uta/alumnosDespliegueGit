package com.soa.alumnos.repository;

import com.soa.alumnos.entity.Alumno;
import com.soa.alumnos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, String> {
    boolean existsBycedula(String cedula);

    List<Alumno> findByCurso(Curso curso);

    List<Alumno> findByCursoId(Long cursoId);

    List<Alumno> findByCedulaContaining(String cedula);
}
