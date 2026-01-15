package com.soa.grade_service.repository;

import com.soa.grade_service.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    // Trouver toutes les notes d'un étudiant
    List<Grade> findByStudentId(Long studentId);

    // Trouver les notes d'un étudiant pour un semestre
    List<Grade> findByStudentIdAndSemester(Long studentId, String semester);

    // Trouver les notes d'une matière
    List<Grade> findBySubject(String subject);
}
