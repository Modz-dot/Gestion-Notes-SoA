package com.soa.student_service.repository;




import com.soa.student_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Méthodes personnalisées
    Optional<Student> findByStudentNumber(String studentNumber);

    Optional<Student> findByEmail(String email);

}