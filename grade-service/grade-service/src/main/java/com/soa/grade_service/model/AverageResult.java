// Cette classe n'est PAS une Entity (pas de @Entity)
// C'est juste pour retourner le résultat du calcul de moyenne

package com.soa.grade_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AverageResult {
    private Long studentId;
    private String semester;
    private Double average;
    private String status; // "PASSED" ou "FAILED"
    private int totalGrades;

}