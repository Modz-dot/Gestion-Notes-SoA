package com.soa.grade_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grades")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de l'étudiant (référence au Student Service)
    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String subject; // Matière : Math, Info...

    @Column(nullable = false)
    private Double score; // Note sur 20

    @Column(nullable = false)
    private Double coefficient; // Coefficient de la matière

    @Column(nullable = false)
    private String semester; // Semestre : S1, S2, S3...

    private String examType; // Type : DS, Examen, TP...


}
