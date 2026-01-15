package com.soa.grade_service.controller;

import com.soa.grade_service.model.Grade;
import com.soa.grade_service.model.AverageResult;
import com.soa.grade_service.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // POST : Créer une note
    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        try {
            Grade created = gradeService.createGrade(grade);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        }
    }


    // GET : Récupérer toutes les notes
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }


    // GET : Récupérer une note par ID
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        if (grade != null) {
            return ResponseEntity.ok(grade);
        }
        return ResponseEntity.notFound().build();
    }


    // GET : Récupérer les notes d'un étudiant
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        return ResponseEntity.ok(grades);
    }


    // GET : Récupérer les notes d'un étudiant pour un semestre
    // URL : http://localhost:8082/api/grades/student/1/semester/S1
    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndSemester(
            @PathVariable Long studentId,
            @PathVariable String semester) {
        List<Grade> grades = gradeService.getGradesByStudentAndSemester(studentId, semester);
        return ResponseEntity.ok(grades);
    }


    // GET : CALCULER LA MOYENNE (ENDPOINT IMPORTANT !)
    // URL : http://localhost:8082/api/grades/student/1/semester/S1/average
    @GetMapping("/student/{studentId}/semester/{semester}/average")
    public ResponseEntity<AverageResult> calculateAverage(
            @PathVariable Long studentId,
            @PathVariable String semester) {
        AverageResult result = gradeService.calculateAverage(studentId, semester);
        return ResponseEntity.ok(result);
    }


    // PUT : Modifier une note
    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id,
                                             @RequestBody Grade grade) {
        Grade updated = gradeService.updateGrade(id, grade);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }


    // DELETE : Supprimer une note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        boolean deleted = gradeService.deleteGrade(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
