package com.soa.grade_service.service;


import com.soa.grade_service.model.Grade;
import com.soa.grade_service.model.AverageResult;
import com.soa.grade_service.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;


    // Créer une note
    public Grade createGrade(Grade grade) {
        // Validation simple
        if (grade.getScore() < 0 || grade.getScore() > 20) {
            throw new IllegalArgumentException("La note doit être entre 0 et 20");
        }
        return gradeRepository.save(grade);
    }


    // Récupérer toutes les notes
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }


    // Récupérer une note par ID
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElse(null);
    }


    // Récupérer les notes d'un étudiant
    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }


    // Récupérer les notes d'un étudiant pour un semestre
    public List<Grade> getGradesByStudentAndSemester(Long studentId, String semester) {
        return gradeRepository.findByStudentIdAndSemester(studentId, semester);
    }



    // CALCUL DE MOYENNE
    public AverageResult calculateAverage(Long studentId, String semester) {
        // 1. Récupérer toutes les notes de l'étudiant pour ce semestre
        List<Grade> grades = gradeRepository.findByStudentIdAndSemester(studentId, semester);

        // 2. Vérifier qu'il y a des notes
        if (grades.isEmpty()) {
            return new AverageResult(studentId, semester, 0.0, "NO_GRADES", 0);
        }

        // 3. Calculer la moyenne pondérée
        double totalScore = 0.0;
        double totalCoefficient = 0.0;

        for (Grade grade : grades) {
            totalScore += grade.getScore() * grade.getCoefficient();
            totalCoefficient += grade.getCoefficient();
        }

        double average = totalScore / totalCoefficient;

        // 4. Déterminer si l'étudiant est validé
        String status = average >= 10.0 ? "PASSED" : "FAILED";

        // 5. Retourner le résultat
        return new AverageResult(studentId, semester,
                Math.round(average * 100.0) / 100.0, // Arrondir à 2 décimales
                status,
                grades.size());
    }



    // Modifier une note
    public Grade updateGrade(Long id, Grade updatedGrade) {
        Grade existing = gradeRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setSubject(updatedGrade.getSubject());
            existing.setScore(updatedGrade.getScore());
            existing.setCoefficient(updatedGrade.getCoefficient());
            existing.setSemester(updatedGrade.getSemester());
            existing.setExamType(updatedGrade.getExamType());
            return gradeRepository.save(existing);
        }
        return null;
    }



    // Supprimer une note
    public boolean deleteGrade(Long id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            return true;
        }
        return false;
    }


}

