package com.soa.student_service.service;

import com.soa.student_service.model.Student;
import com.soa.student_service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    // Créer un étudiant
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }


    // Récupérer tous les étudiants
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    // Récupérer un étudiant par ID
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            return null;
        }
    }


    // Récupérer par numéro d'étudiant
    public Student getStudentByNumber(String studentNumber) {
        Optional<Student> student = studentRepository.findByStudentNumber(studentNumber);
        return student.orElse(null);
    }


    // Modifier un étudiant
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = getStudentById(id);
        if (existing != null) {
            existing.setFirstName(updatedStudent.getFirstName());
            existing.setLastName(updatedStudent.getLastName());
            existing.setEmail(updatedStudent.getEmail());
            existing.setDepartment(updatedStudent.getDepartment());
            return studentRepository.save(existing);
        }
        return null;
    }


    // Supprimer un étudiant
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

